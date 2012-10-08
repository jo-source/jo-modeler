/*
 * Copyright (c) 2012, grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 *   notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 *   notice, this list of conditions and the following disclaimer in the
 *   documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 *   names of its contributors may be used to endorse or promote products
 *   derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL jo-widgets.org BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */

package org.jowidgets.modeler.implementor.neo4j.service;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaQuery;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.lookup.ILookUpEntry;
import org.jowidgets.cap.common.api.lookup.ILookUpEntryBuilder;
import org.jowidgets.cap.common.api.lookup.ILookUpToolkit;
import org.jowidgets.cap.common.api.lookup.ILookUpValueRange;
import org.jowidgets.cap.common.api.lookup.ILookUpValueRangeBuilder;
import org.jowidgets.cap.service.jpa.api.EntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.api.EntityManagerFactoryProvider;
import org.jowidgets.cap.service.jpa.api.IEntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.cap.service.tools.CapServiceProviderBuilder;
import org.jowidgets.cap.service.tools.lookup.StaticLookUpService;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.common.bean.ILookUpElement;
import org.jowidgets.modeler.common.dto.LookUpDisplayFormat;
import org.jowidgets.modeler.service.persistence.bean.LookUp;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;

final class Neo4JImplementorLookUpServicesBuilder {

	private static final IMessage SHORT_LABEL = Messages.getMessage("Neo4JImplementorLookUpServicesBuilder.short");
	private static final IMessage LONG_LABEL = Messages.getMessage("Neo4JImplementorLookUpServicesBuilder.long");

	private static final String SHORT_PROPERTY_NAME = "SHORT";
	private static final String LONG_PROPERTY_NAME = "LONG";

	private static final Map<String, ILookUpValueRange> LOOK_UP_VALUE_RANGES = new HashMap<String, ILookUpValueRange>();

	private Neo4JImplementorLookUpServicesBuilder() {}

	static void registerLookUpServices(final CapServiceProviderBuilder registry, final String entityModelPersistenceUnitName) {
		Assert.paramNotEmpty(entityModelPersistenceUnitName, "entityModelPersistenceUnitName");
		final EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.get(entityModelPersistenceUnitName);
		final IEntityManagerContextTemplate contextTemplate = EntityManagerContextTemplate.create(entityManagerFactory);
		contextTemplate.doInEntityManagerContext(new Runnable() {
			@Override
			public void run() {
				buildInEmContext(registry);
			}
		});
	}

	static ILookUpValueRange getLookUpValueRange(final String lookUpId) {
		return LOOK_UP_VALUE_RANGES.get(lookUpId);
	}

	private static void buildInEmContext(final CapServiceProviderBuilder registry) {
		final EntityManager em = EntityManagerProvider.get();
		final CriteriaQuery<LookUp> criteriaQuery = em.getCriteriaBuilder().createQuery(LookUp.class);
		criteriaQuery.from(LookUp.class);
		for (final LookUp lookUp : em.createQuery(criteriaQuery).getResultList()) {
			registerLookUp(registry, lookUp);
		}
	}

	private static void registerLookUp(final CapServiceProviderBuilder registry, final LookUp lookUp) {
		final ILookUpToolkit lookUpToolkit = CapCommonToolkit.lookUpToolkit();
		final List<ILookUpEntry> entries = new LinkedList<ILookUpEntry>();

		if (lookUp.getHasNullValue()) {
			entries.add(lookUpToolkit.lookUpEntry(null, ""));
		}

		final boolean hasLongValues = hasLongValues(lookUp);

		for (final ILookUpElement element : lookUp.getLookUpElements()) {
			final ILookUpEntryBuilder entryBuilder = lookUpToolkit.lookUpEntryBuilder();
			entryBuilder.setKey(element.getKey());
			if (!EmptyCheck.isEmpty(element.getLabel())) {
				entryBuilder.setValue(SHORT_PROPERTY_NAME, element.getLabel());
			}
			else if (!EmptyCheck.isEmpty(element.getLabelLong())) {
				entryBuilder.setValue(SHORT_PROPERTY_NAME, element.getLabelLong());
			}
			else {
				entryBuilder.setValue(SHORT_PROPERTY_NAME, element.getKey());
			}

			if (hasLongValues) {
				if (!EmptyCheck.isEmpty(element.getLabelLong())) {
					entryBuilder.setValue(LONG_PROPERTY_NAME, element.getLabelLong());
				}
				else if (!EmptyCheck.isEmpty(element.getLabel())) {
					entryBuilder.setValue(LONG_PROPERTY_NAME, element.getLabel());
				}
				else {
					entryBuilder.setValue(LONG_PROPERTY_NAME, element.getKey());
				}
			}

			entryBuilder.setDescription(element.getDescription());
			entryBuilder.setValid(element.getIsValid().booleanValue());

			entries.add(entryBuilder.build());
		}

		registry.addLookUpService(lookUp.getName(), new StaticLookUpService(entries));
		LOOK_UP_VALUE_RANGES.put(lookUp.getName(), createValueRange(lookUp, hasLongValues));
	}

	private static boolean hasLongValues(final LookUp lookUp) {
		for (final ILookUpElement element : lookUp.getLookUpElements()) {
			if (!EmptyCheck.isEmpty(element.getLabelLong())) {
				return true;
			}
		}
		return false;
	}

	private static ILookUpValueRange createValueRange(final LookUp lookUp, final boolean hasLongValues) {
		final ILookUpToolkit lookUpToolkit = CapCommonToolkit.lookUpToolkit();
		final ILookUpValueRangeBuilder builder = lookUpToolkit.lookUpValueRangeBuilder();
		builder.setLookUpId(lookUp.getName());
		if (lookUp.getDefaultDisplayFormat() == LookUpDisplayFormat.SHORT) {
			builder.setDefaultValuePropertyName(SHORT_PROPERTY_NAME);
		}
		else {
			builder.setDefaultValuePropertyName(LONG_PROPERTY_NAME);
		}
		builder.addValueProperty(lookUpToolkit.lookUpProperty(SHORT_PROPERTY_NAME, SHORT_LABEL.get()));
		if (hasLongValues) {
			builder.addValueProperty(lookUpToolkit.lookUpProperty(LONG_PROPERTY_NAME, LONG_LABEL.get()));
		}
		return builder.build();
	}
}
