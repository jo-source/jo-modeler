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

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jowidgets.cap.service.jpa.api.EntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.api.EntityManagerFactoryProvider;
import org.jowidgets.cap.service.jpa.api.IEntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.cap.service.tools.entity.EntityApplicationServiceBuilder;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.util.Assert;

public final class Neo4JImplementorEntityApplicationServiceBuilder extends EntityApplicationServiceBuilder {

	public Neo4JImplementorEntityApplicationServiceBuilder(final String entityModelPersistenceUnitName) {
		Assert.paramNotEmpty(entityModelPersistenceUnitName, "entityModelPersistenceUnitName");
		final EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.get(entityModelPersistenceUnitName);
		final IEntityManagerContextTemplate contextTemplate = EntityManagerContextTemplate.create(entityManagerFactory);
		contextTemplate.doInEntityManagerContext(new Runnable() {
			@Override
			public void run() {
				buildInEmContext();
			}
		});

		setLabel("Application");
	}

	private void buildInEmContext() {
		final EntityManager em = EntityManagerProvider.get();
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		final CriteriaQuery<EntityModel> query = criteriaBuilder.createQuery(EntityModel.class);
		final Root<EntityModel> root = query.from(EntityModel.class);
		query.orderBy(criteriaBuilder.asc(root.get(IEntityModel.ORDER_PROPERTY)));
		for (final EntityModel entityModel : em.createQuery(query).getResultList()) {
			addNode(entityModel.getName());
		}
	}

}
