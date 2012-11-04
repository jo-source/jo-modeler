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
import org.jowidgets.modeler.common.bean.IEntityPropertyModel;
import org.jowidgets.modeler.common.bean.IIcon;
import org.jowidgets.modeler.common.bean.ILookUpElement;
import org.jowidgets.modeler.common.bean.IRelationModel;
import org.jowidgets.modeler.common.entity.EntityIds;
import org.jowidgets.modeler.service.descriptor.DestinationEntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.EntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.EntityPropertyModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.IconDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.IconSetDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.LookUpDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.LookUpElementDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.RelationModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.descriptor.SourceEntityModelDtoDescriptorBuilder;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityPropertyModel;
import org.jowidgets.modeler.service.persistence.bean.Icon;
import org.jowidgets.modeler.service.persistence.bean.IconSet;
import org.jowidgets.modeler.service.persistence.bean.LookUp;
import org.jowidgets.modeler.service.persistence.bean.LookUpElement;
import org.jowidgets.modeler.service.persistence.bean.RelationModel;
import org.jowidgets.service.api.IServiceRegistry;

public final class ModelerEntityServiceBuilder extends JpaEntityServiceBuilderWrapper {

	public ModelerEntityServiceBuilder(final IServiceRegistry registry) {
		super(registry);

		//IEntityModel
		IBeanEntityBluePrint bp = addEntity().setEntityId(EntityIds.ENTITY_MODEL).setBeanType(EntityModel.class);
		bp.setDtoDescriptor(new EntityModelDtoDescriptorBuilder());
		addEntityModelLinkDescriptors(bp);

		//IEntityPropertyModel
		bp = addEntity().setEntityId(EntityIds.ENTITY_PROPERTY_MODEL).setBeanType(EntityPropertyModel.class);
		bp.setDtoDescriptor(new EntityPropertyModelDtoDescriptorBuilder());

		//IRelationModel
		bp = addEntity().setEntityId(EntityIds.RELATION_MODEL).setBeanType(RelationModel.class);
		bp.setDtoDescriptor(new RelationModelDtoDescriptorBuilder());
		addRelationModelLinkDescriptors(bp);

		//ILookUp
		bp = addEntity().setEntityId(EntityIds.LOOK_UP).setBeanType(LookUp.class);
		bp.setDtoDescriptor(new LookUpDtoDescriptorBuilder());
		addLookUpLookUpElementLinkDescriptor(bp);

		//IIconSet
		bp = addEntity().setEntityId(EntityIds.ICON_SET).setBeanType(IconSet.class);
		bp.setDtoDescriptor(new IconSetDtoDescriptorBuilder());
		addIconSetIconsLinkDescriptor(bp);

		//IIcon
		bp = addEntity().setEntityId(EntityIds.ICON).setBeanType(Icon.class);
		bp.setDtoDescriptor(new IconDtoDescriptorBuilder());

		//Linked property models of entity models
		bp = addEntity().setEntityId(EntityIds.LINKED_ENTITY_PROPERTY_MODEL_OF_ENTITY_MODEL);
		bp.setBeanType(EntityPropertyModel.class);
		bp.setDtoDescriptor(new EntityPropertyModelDtoDescriptorBuilder());
		bp.setReaderService(createEntityPropertyModelOfEntityModelReader());
		bp.setProperties(IEntityPropertyModel.ALL_PROPERTIES);

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

		//Linked look up elements of look up
		bp = addEntity().setEntityId(EntityIds.LINKED_LOOK_UP_ELEMENTS_OF_LOOK_UP);
		bp.setBeanType(LookUpElement.class);
		bp.setDtoDescriptor(new LookUpElementDtoDescriptorBuilder());
		bp.setReaderService(createLookUpElementsOfLookUpReader());
		bp.setProperties(ILookUpElement.ALL_PROPERTIES);

		//Linked icons of icon set
		bp = addEntity().setEntityId(EntityIds.LINKED_ICONS_OF_ICON_SET);
		bp.setBeanType(Icon.class);
		bp.setDtoDescriptor(new IconDtoDescriptorBuilder());
		bp.setReaderService(createIconsOfIconSetReader());
		bp.setProperties(IIcon.ALL_PROPERTIES);
	}

	private void addEntityModelLinkDescriptors(final IBeanEntityBluePrint bp) {
		addEntityPropertyModelLinkDescriptor(bp);
		addEntityModelRelationModelLinkDescriptor(bp);
	}

	private void addRelationModelLinkDescriptors(final IBeanEntityBluePrint bp) {
		addSourceEntityModelOfRelationModelLinkDescriptor(bp);
		addDestinationEntityModelOfRelationModelLinkDescriptor(bp);
	}

	private void addEntityPropertyModelLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.LINKED_ENTITY_PROPERTY_MODEL_OF_ENTITY_MODEL);
		bp.setLinkBeanType(EntityPropertyModel.class);
		bp.setLinkedEntityId(EntityIds.LINKED_ENTITY_PROPERTY_MODEL_OF_ENTITY_MODEL);
		bp.setSourceProperties(EntityPropertyModel.PARENT_MODEL_ID_PROPERTY);
		bp.setLinkDeleterService(null);
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

	private void addLookUpLookUpElementLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.LINKED_LOOK_UP_ELEMENTS_OF_LOOK_UP);
		bp.setLinkBeanType(LookUpElement.class);
		bp.setLinkedEntityId(EntityIds.LINKED_LOOK_UP_ELEMENTS_OF_LOOK_UP);
		bp.setSourceProperties(LookUpElement.LOOK_UP_ID_PROPERTY);
		bp.setLinkDeleterService(null);
	}

	private void addIconSetIconsLinkDescriptor(final IBeanEntityBluePrint entityBp) {
		final IBeanEntityLinkBluePrint bp = entityBp.addLink();
		bp.setLinkEntityId(EntityIds.LINKED_ICONS_OF_ICON_SET);
		bp.setLinkBeanType(LookUpElement.class);
		bp.setLinkedEntityId(EntityIds.LINKED_ICONS_OF_ICON_SET);
		bp.setSourceProperties(Icon.ICON_SET_ID_PROPERTY);
		bp.setLinkCreatorService(null);
		bp.setLinkDeleterService(null);
	}

	private IReaderService<Void> createEntityPropertyModelOfEntityModelReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(EntityPropertyModel.class);
		queryBuilder.setParentPropertyPath("parentModel");
		return getServiceFactory().readerService(
				EntityPropertyModel.class,
				queryBuilder.build(),
				IEntityPropertyModel.ALL_PROPERTIES);
	}

	private IReaderService<Void> createLookUpElementsOfLookUpReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(LookUpElement.class);
		queryBuilder.setParentPropertyPath("lookUp");
		return getServiceFactory().readerService(LookUpElement.class, queryBuilder.build(), ILookUpElement.ALL_PROPERTIES);
	}

	private IReaderService<Void> createIconsOfIconSetReader() {
		final ICriteriaQueryCreatorBuilder<Void> queryBuilder = JpaQueryToolkit.criteriaQueryCreatorBuilder(Icon.class);
		queryBuilder.setParentPropertyPath("iconSet");
		return getServiceFactory().readerService(Icon.class, queryBuilder.build(), IIcon.ALL_PROPERTIES);
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
