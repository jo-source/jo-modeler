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

package org.jowidgets.modeler.service.entity;

import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.service.api.entity.IBeanEntityBluePrint;
import org.jowidgets.cap.service.api.entity.IBeanEntityLinkBluePrint;
import org.jowidgets.cap.service.jpa.api.query.ICriteriaQueryCreatorBuilder;
import org.jowidgets.cap.service.jpa.api.query.JpaQueryToolkit;
import org.jowidgets.cap.service.jpa.tools.entity.JpaEntityServiceBuilderWrapper;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.common.bean.IEntityModelPropertyModelLink;
import org.jowidgets.modeler.common.bean.IPropertyModel;
import org.jowidgets.modeler.common.bean.IRelationModel;
import org.jowidgets.modeler.common.entity.EntityIds;
import org.jowidgets.modeler.service.descriptor.DestinationEntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.EntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.PropertyModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.RelationModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.SourceEntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityModelPropertyModelLink;
import org.jowidgets.modeler.service.persistence.bean.PropertyModel;
import org.jowidgets.modeler.service.persistence.bean.RelationModel;
import org.jowidgets.service.api.IServiceRegistry;

public final class ModelerEntityServiceBuilder extends JpaEntityServiceBuilderWrapper {

	public ModelerEntityServiceBuilder(final IServiceRegistry registry) {
		super(registry);

		//IEntityModel
		IBeanEntityBluePrint bp = addEntity().setEntityId(EntityIds.ENTITY_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new EntityModelDtoDescriptorBuilder());
		addEntityModelLinkDescriptors(bp);

		//IPropertyModel
		bp = addEntity().setEntityId(EntityIds.PROPERTY_MODEL).setBeanType(PropertyModel.class);
		bp.setDtoDescriptor(new PropertyModelDtoDescriptorBuilder());
		addPropertyModelLinkDescriptors(bp);

		//IRelationModel
		bp = addEntity().setEntityId(EntityIds.RELATION_MODEL).setBeanType(RelationModel.class);
		bp.setDtoDescriptor(new RelationModelDtoDescriptorBuilder());
		addRelationModelLinkDescriptors(bp);

		//Linked entity models of property models
		bp = addEntity().setEntityId(EntityIds.LINKED_ENTITY_MODEL_OF_PROPERTY_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new EntityModelDtoDescriptorBuilder());
		bp.setReaderService(createEntityModelOfPropertyModelReader(true));
		addEntityModelLinkDescriptors(bp);

		//Linkable entity models of property models
		bp = addEntity().setEntityId(EntityIds.LINKABLE_ENTITY_MODEL_OF_PROPERTY_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new EntityModelDtoDescriptorBuilder());
		bp.setReaderService(createEntityModelOfPropertyModelReader(false));
		bp.setReadonly();

		//Linked property models of entity models
		bp = addEntity().setEntityId(EntityIds.LINKED_PROPERTY_MODEL_OF_ENTITY_MODEL).setBeanType(PropertyModel.class);
		bp.setDtoDescriptor(new PropertyModelDtoDescriptorBuilder());
		bp.setReaderService(createPropertyModelOfEntityModelReader(true));
		addPropertyModelLinkDescriptors(bp);

		//Linkable property models of entity models
		bp = addEntity().setEntityId(EntityIds.LINKABLE_PROPERTY_MODEL_OF_ENTITY_MODEL).setBeanType(PropertyModel.class);
		bp.setDtoDescriptor(new PropertyModelDtoDescriptorBuilder());
		bp.setReaderService(createPropertyModelOfEntityModelReader(false));
		bp.setReadonly();

		//Linked relation model of entity model
		bp = addEntity().setEntityId(EntityIds.LINKED_RELATION_MODEL_OF_ENTITY_MODEL).setBeanType(RelationModel.class);
		bp.setDtoDescriptor(new RelationModelDtoDescriptorBuilder());
		bp.setReaderService(createLinkedRelationModelOfEntityModelReader());
		addRelationModelLinkDescriptors(bp);

		//Linkable entity model of entity model
		bp = addEntity().setEntityId(EntityIds.LINKABLE_ENTITY_MODEL_OF_ENTITY_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new EntityModelDtoDescriptorBuilder());
		bp.setReadonly();

		//Source entity model of relation model
		bp = addEntity().setEntityId(EntityIds.SOURCE_ENTITY_MODEL_OF_RELATION_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new SourceEntityModelDtoDescriptorBuilder());
		bp.setReaderService(createSourceEntityModelOfRelationModelReader());
		addEntityModelLinkDescriptors(bp);

		//Destination entity model of relation model
		bp = addEntity().setEntityId(EntityIds.DESTINATION_ENTITY_MODEL_OF_RELATION_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new DestinationEntityModelDtoDescriptorBuilder());
		bp.setReaderService(createDestinationEntityModelOfRelationModelReader());
		addEntityModelLinkDescriptors(bp);
	}

	private void addEntityModelLinkDescriptors(final IBeanEntityBluePrint bp) {
		addEntityModelPropertyModelLinkDescriptor(bp);
		addEntityModelRelationModelLinkDescriptor(bp);
	}

	private void addPropertyModelLinkDescriptors(final IBeanEntityBluePrint bp) {
		addPropertyModelEntityModelLinkDescriptor(bp);
	}

	private void addRelationModelLinkDescriptors(final IBeanEntityBluePrint bp) {
		addSourceEntityModelOfRelationModelLinkDescriptor(bp);
		addDestinationEntityModelOfRelationModelLinkDescriptor(bp);
	}

	private void addEntityModelPropertyModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.ENTITY_MODEL_PROPERTY_MODEL_LINK);
		bp.setLinkBeanType(EntityModelPropertyModelLink.class);
		bp.setLinkedEntityId(EntityIds.LINKED_PROPERTY_MODEL_OF_ENTITY_MODEL);
		bp.setLinkableEntityId(EntityIds.LINKABLE_PROPERTY_MODEL_OF_ENTITY_MODEL);
		bp.setSourceProperties(IEntityModelPropertyModelLink.ENTITY_MODEL_ID_PROPERTY);
		bp.setDestinationProperties(IEntityModelPropertyModelLink.PROPERTY_MODEL_ID_PROPERTY);
	}

	private void addPropertyModelEntityModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.ENTITY_MODEL_PROPERTY_MODEL_LINK);
		bp.setLinkBeanType(EntityModelPropertyModelLink.class);
		bp.setLinkedEntityId(EntityIds.LINKED_ENTITY_MODEL_OF_PROPERTY_MODEL);
		bp.setLinkableEntityId(EntityIds.LINKABLE_ENTITY_MODEL_OF_PROPERTY_MODEL);
		bp.setSourceProperties(IEntityModelPropertyModelLink.PROPERTY_MODEL_ID_PROPERTY);
		bp.setDestinationProperties(IEntityModelPropertyModelLink.ENTITY_MODEL_ID_PROPERTY);
	}

	private void addEntityModelRelationModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.RELATION_MODEL);
		bp.setLinkBeanType(RelationModel.class);
		bp.setLinkedEntityId(EntityIds.LINKED_RELATION_MODEL_OF_ENTITY_MODEL);
		bp.setLinkableEntityId(EntityIds.LINKABLE_ENTITY_MODEL_OF_ENTITY_MODEL);
		bp.setSourceProperties(IRelationModel.SOURCE_ENTITY_MODEL_ID_PROPERTY);
		bp.setDestinationProperties(IRelationModel.DESTINATION_ENTITY_MODEL_ID_PROPERTY);
		bp.setLinkDeleterService(null);
	}

	private void addSourceEntityModelOfRelationModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkedEntityId(EntityIds.SOURCE_ENTITY_MODEL_OF_RELATION_MODEL);
	}

	private void addDestinationEntityModelOfRelationModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkedEntityId(EntityIds.DESTINATION_ENTITY_MODEL_OF_RELATION_MODEL);
	}

	private IReaderService<Void> createPropertyModelOfEntityModelReader(final boolean linked) {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(PropertyModel.class);
		queryBuilder.setParentPropertyPath(linked, "entityModelPropertyModelLinks", "entityModel");
		return getServiceFactory().readerService(PropertyModel.class, queryBuilder.build(), IPropertyModel.ALL_PROPERTIES);
	}

	private IReaderService<Void> createEntityModelOfPropertyModelReader(final boolean linked) {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(EntityModel.class);
		queryBuilder.setParentPropertyPath(linked, "entityModelPropertyModelLinks", "propertyModel");
		return getServiceFactory().readerService(EntityModel.class, queryBuilder.build(), IEntityModel.ALL_PROPERTIES);
	}

	private IReaderService<Void> createLinkedRelationModelOfEntityModelReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(RelationModel.class);
		queryBuilder.addParentPropertyPath(true, "sourceEntityModel");
		queryBuilder.addParentPropertyPath(true, "destinationEntityModel");
		return getServiceFactory().readerService(RelationModel.class, queryBuilder.build(), IRelationModel.ALL_PROPERTIES);
	}

	private IReaderService<Void> createSourceEntityModelOfRelationModelReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(EntityModel.class);
		queryBuilder.addParentPropertyPath(true, "sourceEntityOfDestinationEntityRelation");
		return getServiceFactory().readerService(EntityModel.class, queryBuilder.build(), IEntityModel.ALL_PROPERTIES);
	}

	private IReaderService<Void> createDestinationEntityModelOfRelationModelReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(EntityModel.class);
		queryBuilder.addParentPropertyPath(true, "destinationEntityOfSourceEntityRelation");
		return getServiceFactory().readerService(EntityModel.class, queryBuilder.build(), IEntityModel.ALL_PROPERTIES);
	}

}
