/*
 * Copyright (c) 2013, grossmann
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

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.service.jpa.api.EntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.api.EntityManagerFactoryProvider;
import org.jowidgets.cap.service.jpa.api.IEntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.cap.service.neo4j.api.IBeanFactory;
import org.jowidgets.cap.service.neo4j.api.INodeBean;
import org.jowidgets.cap.service.neo4j.api.IRelationshipBean;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityPropertyModel;
import org.jowidgets.util.Assert;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

final class Neo4JImplementorBeanFactory implements IBeanFactory {

	private final Map<String, Map<String, String>> propertyOfEntities;

	Neo4JImplementorBeanFactory(final String entityModelPersistenceUnitName) {
		Assert.paramNotEmpty(entityModelPersistenceUnitName, "entityModelPersistenceUnitName");
		final EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.get(entityModelPersistenceUnitName);
		final IEntityManagerContextTemplate contextTemplate = EntityManagerContextTemplate.create(entityManagerFactory);
		this.propertyOfEntities = new HashMap<String, Map<String, String>>();
		contextTemplate.doInEntityManagerContext(new Runnable() {
			@Override
			public void run() {
				initInEmContext();
			}
		});
	}

	private void initInEmContext() {
		final EntityManager em = EntityManagerProvider.get();
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		final CriteriaQuery<EntityModel> query = criteriaBuilder.createQuery(EntityModel.class);
		final Root<EntityModel> root = query.from(EntityModel.class);
		query.orderBy(criteriaBuilder.asc(root.get(IEntityModel.ORDER_PROPERTY)));
		for (final EntityModel entityModel : em.createQuery(query).getResultList()) {
			final Map<String, String> properties = new HashMap<String, String>();
			propertyOfEntities.put(entityModel.getName(), properties);
			for (final EntityPropertyModel propertyModel : entityModel.getEntityPropertyModels()) {
				properties.put(propertyModel.getName(), propertyModel.getValueType());
			}
		}
	}

	@Override
	public <BEAN_TYPE extends IBean> BEAN_TYPE createNodeBean(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Node node) {
		Assert.paramNotNull(beanType, "beanType");
		Assert.paramNotNull(beanTypeId, "beanTypeId");
		Assert.paramNotNull(node, "node");

		return createNodeBeanInstance(beanType, beanTypeId, node);
	}

	@Override
	public <BEAN_TYPE extends IBean> BEAN_TYPE createRelatedNodeBean(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Node node,
		final Relationship relationship) {

		return createRelatedNodeBeanInstance(beanType, beanTypeId, node, relationship);
	}

	@Override
	public <BEAN_TYPE extends IBean> BEAN_TYPE createRelationshipBean(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Relationship relationship) {
		return createRelationshipBeanInstance(beanType, beanTypeId, relationship);
	}

	@Override
	public <BEAN_TYPE extends IBean> boolean isNodeBean(final Class<BEAN_TYPE> beanType, final Object beanTypeId) {
		Assert.paramNotNull(beanType, "beanType");
		Assert.paramNotNull(beanTypeId, "beanTypeId");
		return INodeBean.class.isAssignableFrom(beanType);
	}

	@Override
	public <BEAN_TYPE extends IBean> boolean isRelationshipBean(final Class<BEAN_TYPE> beanType, final Object beanTypeId) {
		Assert.paramNotNull(beanType, "beanType");
		Assert.paramNotNull(beanTypeId, "beanTypeId");
		return IRelationshipBean.class.isAssignableFrom(beanType);
	}

	private <BEAN_TYPE extends IBean> BEAN_TYPE createRelatedNodeBeanInstance(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Node node,
		final Relationship relationship) {
		try {
			final Constructor<BEAN_TYPE> constructor = beanType.getConstructor(Node.class, Relationship.class);
			return constructor.newInstance(node, relationship);
		}
		catch (final Exception e) {
			return createNodeBeanInstance(beanType, beanTypeId, node);
		}
	}

	@SuppressWarnings("unchecked")
	private <BEAN_TYPE extends IBean> BEAN_TYPE createNodeBeanInstance(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Node node) {

		return (BEAN_TYPE) new Neo4JImplementorBeanPropertyMapNodeBean(node, propertyOfEntities.get(beanTypeId));
	}

	private <BEAN_TYPE extends IBean> BEAN_TYPE createRelationshipBeanInstance(
		final Class<BEAN_TYPE> beanType,
		final Object beanTypeId,
		final Relationship relationship) {
		try {
			final Constructor<BEAN_TYPE> constructor = beanType.getConstructor(Relationship.class);
			return constructor.newInstance(relationship);
		}
		catch (final Exception e) {
			throw new RuntimeException(e);
		}
	}

}
