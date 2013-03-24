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

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptor;
import org.jowidgets.cap.common.api.bean.IProperty;
import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.service.api.entity.IBeanEntityBluePrint;
import org.jowidgets.cap.service.api.entity.IBeanEntityLinkBluePrint;
import org.jowidgets.cap.service.jpa.api.EntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.api.EntityManagerFactoryProvider;
import org.jowidgets.cap.service.jpa.api.IEntityManagerContextTemplate;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.cap.service.neo4j.tools.Neo4JEntityServiceBuilderWrapper;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.modeler.service.persistence.bean.RelationModel;
import org.jowidgets.service.api.IServiceRegistry;
import org.jowidgets.util.Assert;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.NullCompatibleComparison;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.RelationshipType;

public final class Neo4JImplementorEntityServiceBuilder extends Neo4JEntityServiceBuilderWrapper {

	public Neo4JImplementorEntityServiceBuilder(final IServiceRegistry registry, final String entityModelPersistenceUnitName) {
		super(registry);
		Assert.paramNotEmpty(entityModelPersistenceUnitName, "entityModelPersistenceUnitName");
		final EntityManagerFactory entityManagerFactory = EntityManagerFactoryProvider.get(entityModelPersistenceUnitName);
		final IEntityManagerContextTemplate contextTemplate = EntityManagerContextTemplate.create(entityManagerFactory);
		contextTemplate.doInEntityManagerContext(new Runnable() {
			@Override
			public void run() {
				buildInEmContext();
			}
		});
	}

	private void buildInEmContext() {
		final EntityManager em = EntityManagerProvider.get();
		final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
		final CriteriaQuery<EntityModel> query = criteriaBuilder.createQuery(EntityModel.class);
		final Root<EntityModel> root = query.from(EntityModel.class);
		query.orderBy(criteriaBuilder.asc(root.get(IEntityModel.ORDER_PROPERTY)));
		for (final EntityModel entityModel : em.createQuery(query).getResultList()) {
			addEntityModel(entityModel);
		}
	}

	private void addEntityModel(final EntityModel entityModel) {
		final IBeanEntityBluePrint bp = addEntity();
		bp.setEntityId(entityModel.getName());
		bp.setBeanType(Neo4JImplementorBeanPropertyMapNodeBean.class);
		bp.setBeanTypeId(entityModel.getName());
		bp.setDtoDescriptor(EntityModelDtoDescriptorBuilder.create(entityModel));
		addLinkedEntities(bp, entityModel, true);
	}

	private void addLinkedEntities(final IBeanEntityBluePrint bp, final EntityModel entityModel, final boolean createEntities) {
		final List<RelationDescriptor> descriptors = new LinkedList<RelationDescriptor>();
		for (final RelationModel relation : entityModel.getSourceEntityOfDestinationEntityRelation()) {
			descriptors.add(new RelationDescriptor(
				entityModel,
				relation.getDestinationEntityModel(),
				relation,
				relation.getLabel(),
				false));
		}
		for (final RelationModel relation : entityModel.getDestinationEntityOfSourceEntityRelation()) {
			if (!relation.getSymmetric().booleanValue() && !EmptyCheck.isEmpty(relation.getInverseLabel())) {
				descriptors.add(new RelationDescriptor(
					entityModel,
					relation.getSourceEntityModel(),
					relation,
					relation.getInverseLabel(),
					true));
			}
		}
		Collections.sort(descriptors);
		for (final RelationDescriptor descriptor : descriptors) {
			addLinkedEntity(bp, createEntities, descriptor);
		}
	}

	private void addLinkedEntity(final IBeanEntityBluePrint bp, final boolean createEntities, final RelationDescriptor descriptor) {

		final EntityModel entity = descriptor.getEntity();
		final EntityModel linkedEntity = descriptor.getLinkedEntity();
		final RelationModel relation = descriptor.getRelation();
		final String linkedLabel = descriptor.getLinkedLabel();
		final boolean inverse = descriptor.isInverse();

		final String entityIdSuffix = entity.getName() + relation.getName() + linkedEntity.getName();
		final String inverseSuffix = inverse ? "Inverse" : "";

		final String linkEntityId = "Link" + entityIdSuffix + inverseSuffix;
		final String linkedEntityId = "Linked" + entityIdSuffix + inverseSuffix;
		final String linkableEntityId = "Linkable" + entityIdSuffix + inverseSuffix;

		final DynamicRelationshipType relationship = new DynamicRelationshipType(relation);

		final IBeanDtoDescriptor linkedDtoDescriptor = EntityModelDtoDescriptorBuilder.create(
				linkedEntity,
				linkedLabel,
				linkedLabel);
		final IBeanDtoDescriptor linkableDtoDescriptor = EntityModelDtoDescriptorBuilder.create(linkedEntity);
		final Collection<String> linkedProperties = getProperties(linkedDtoDescriptor);

		final IBeanEntityLinkBluePrint link = bp.addLink();
		link.setLinkEntityId(linkEntityId);
		link.setLinkBeanType(DynamicRelationshipBean.class);
		link.setLinkBeanTypeId(relationship.getName());
		link.setLinkedEntityId(linkedEntityId);
		link.setLinkableEntityId(linkableEntityId);
		link.setSymmetric(relation.getSymmetric().booleanValue());

		if (!inverse) {
			link.setSourceProperties(DynamicRelationshipBean.SOURCE_ID_PROPERTY_PREFIX
				+ ":"
				+ entity.getName()
				+ ":"
				+ relationship.getName());

			link.setDestinationProperties(DynamicRelationshipBean.DESTINATION_ID_PROPERTY_PREFIX
				+ ":"
				+ linkedEntity.getName()
				+ ":"
				+ relationship.getName());
		}
		else {
			link.setSourceProperties(DynamicRelationshipBean.DESTINATION_ID_PROPERTY_PREFIX
				+ ":"
				+ entity.getName()
				+ ":"
				+ relationship.getName());

			link.setDestinationProperties(DynamicRelationshipBean.SOURCE_ID_PROPERTY_PREFIX
				+ ":"
				+ linkedEntity.getName()
				+ ":"
				+ relationship.getName());
		}

		if (createEntities) {
			final Direction direction;
			if (relation.getSymmetric().booleanValue()) {
				direction = Direction.BOTH;
			}
			else {
				direction = inverse ? Direction.INCOMING : Direction.OUTGOING;
			}

			//create linked
			final IBeanEntityBluePrint linkedBp = addEntity();
			linkedBp.setEntityId(linkedEntityId);
			linkedBp.setBeanType(Neo4JImplementorBeanPropertyMapNodeBean.class);
			linkedBp.setBeanTypeId(linkedEntity.getName());
			linkedBp.setDtoDescriptor(linkedDtoDescriptor);
			final IReaderService<Void> linkedReaderService = getServiceFactory().relatedReaderService(
					entity.getName(),
					Neo4JImplementorBeanPropertyMapNodeBean.class,
					linkedEntity.getName(),
					relationship,
					direction,
					true,
					linkedProperties);
			linkedBp.setReaderService(linkedReaderService);
			addLinkedEntities(linkedBp, linkedEntity, false);

			//create linkable
			final IBeanEntityBluePrint linkableBp = addEntity();
			linkableBp.setEntityId(linkableEntityId);
			linkableBp.setBeanType(Neo4JImplementorBeanPropertyMapNodeBean.class);
			linkableBp.setBeanTypeId(linkedEntity.getName());
			linkableBp.setDtoDescriptor(linkableDtoDescriptor);
			final IReaderService<Void> linkableReaderService = getServiceFactory().relatedReaderService(
					entity.getName(),
					Neo4JImplementorBeanPropertyMapNodeBean.class,
					linkedEntity.getName(),
					relationship,
					direction,
					false,
					linkedProperties);
			linkableBp.setReaderService(linkableReaderService);
		}
	}

	private static Collection<String> getProperties(final IBeanDtoDescriptor descriptor) {
		final List<String> result = new LinkedList<String>();
		for (final IProperty property : descriptor.getProperties()) {
			result.add(property.getName());
		}
		return result;
	}

	private static final class DynamicRelationshipType implements RelationshipType {

		private final String sourceEntityName;
		private final String destinationEntityName;
		private final String name;

		private DynamicRelationshipType(final RelationModel relation) {
			this.sourceEntityName = relation.getSourceEntityModel().getName();
			this.destinationEntityName = relation.getDestinationEntityModel().getName();
			this.name = sourceEntityName + relation.getName() + destinationEntityName;
		}

		@Override
		public String name() {
			return name;
		}

		private String getName() {
			return name;
		}

	}

	private static final class RelationDescriptor implements Comparable<RelationDescriptor> {

		private final EntityModel entity;
		private final EntityModel linkedEntity;
		private final RelationModel relation;
		private final String linkedLabel;
		private final boolean inverse;

		private RelationDescriptor(
			final EntityModel entity,
			final EntityModel linkedEntity,
			final RelationModel relation,
			final String linkedLabel,
			final boolean inverse) {

			this.entity = entity;
			this.linkedEntity = linkedEntity;
			this.relation = relation;
			this.linkedLabel = linkedLabel;
			this.inverse = inverse;
		}

		private EntityModel getEntity() {
			return entity;
		}

		private EntityModel getLinkedEntity() {
			return linkedEntity;
		}

		private String getLinkedLabel() {
			return linkedLabel;
		}

		private RelationModel getRelation() {
			return relation;
		}

		private boolean isInverse() {
			return inverse;
		}

		@Override
		public int compareTo(final RelationDescriptor relationDescriptor) {
			final int relationOrderResult = NullCompatibleComparison.compareTo(
					getRelationOrder(),
					relationDescriptor.getRelationOrder());
			if (relationOrderResult != 0) {
				return relationOrderResult;
			}
			return linkedLabel.compareTo(relationDescriptor.getLinkedLabel());
		}

		private Integer getRelationOrder() {
			if (!inverse || relation.getSymmetric().booleanValue()) {
				return relation.getDestinationOrder();

			}
			else {
				return relation.getSourceOrder();
			}
		}

	}
}
