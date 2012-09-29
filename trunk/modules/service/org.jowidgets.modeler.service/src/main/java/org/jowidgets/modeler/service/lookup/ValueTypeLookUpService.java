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

package org.jowidgets.modeler.service.lookup;

import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.execution.IExecutionCallback;
import org.jowidgets.cap.common.api.lookup.ILookUpEntry;
import org.jowidgets.cap.common.api.lookup.ILookUpToolkit;
import org.jowidgets.cap.service.api.adapter.ISyncLookUpService;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;

public final class ValueTypeLookUpService implements ISyncLookUpService {

	public static final String ENTITY_VALUE_TYPE_KEY_PREFIX = "ENTITY_VALUE_TYPE_KEY:";
	public static final String STRING_KEY = String.class.getName();
	public static final String LONG_KEY = Long.class.getName();
	public static final String INTEGER_KEY = Integer.class.getName();
	public static final String DOUBLE_KEY = Double.class.getName();
	public static final String DATE_KEY = Date.class.getName();
	public static final String BOOLEAN_KEY = Boolean.class.getName();

	@Override
	public List<ILookUpEntry> readValues(final IExecutionCallback executionCallback) {
		final ILookUpToolkit lookUpToolkit = CapCommonToolkit.lookUpToolkit();
		final List<ILookUpEntry> result = new LinkedList<ILookUpEntry>();

		result.add(lookUpToolkit.lookUpEntry(STRING_KEY, "String"));
		result.add(lookUpToolkit.lookUpEntry(INTEGER_KEY, "Integer"));
		result.add(lookUpToolkit.lookUpEntry(LONG_KEY, "Long"));
		result.add(lookUpToolkit.lookUpEntry(DOUBLE_KEY, "Double"));
		result.add(lookUpToolkit.lookUpEntry(BOOLEAN_KEY, "Boolean"));
		result.add(lookUpToolkit.lookUpEntry(DATE_KEY, "Date"));

		final EntityManager em = EntityManagerProvider.get();

		final CriteriaQuery<EntityModel> criteriaQuery = em.getCriteriaBuilder().createQuery(EntityModel.class);
		criteriaQuery.from(EntityModel.class);

		for (final EntityModel entity : em.createQuery(criteriaQuery).getResultList()) {
			result.add(lookUpToolkit.lookUpEntry(ENTITY_VALUE_TYPE_KEY_PREFIX + entity.getName(), entity.getLabelSingular()));
		}

		return Collections.unmodifiableList(result);
	}

}
