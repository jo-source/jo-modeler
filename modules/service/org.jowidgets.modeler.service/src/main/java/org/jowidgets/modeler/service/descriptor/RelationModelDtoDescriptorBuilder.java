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

package org.jowidgets.modeler.service.descriptor;

import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.cap.common.api.bean.IBeanPropertyBluePrint;
import org.jowidgets.cap.common.api.sort.Sort;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.common.bean.IRelationModel;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.modeler.common.icons.ModelerIconsCommon;
import org.jowidgets.modeler.common.lookup.LookUpIds;
import org.jowidgets.modeler.service.lookup.CardinalityLookUpService;
import org.jowidgets.util.Assert;

public class RelationModelDtoDescriptorBuilder extends AbstractDtoDescriptorBuilder {

	public RelationModelDtoDescriptorBuilder() {
		this("label.singular", "label.plural");
	}

	public RelationModelDtoDescriptorBuilder(final String labelSingularKey, final String labelPluralKey) {
		super(IRelationModel.class);

		setLabelSingular(getMessage(labelSingularKey));
		setLabelPlural(getMessage(labelPluralKey));
		setRenderingPattern("$" + IRelationModel.LABEL_PROPERTY + "$ - $" + IRelationModel.INVERSE_PROPERTY + "$");
		setIconDescriptor(ModelerIconsCommon.RELATION_MODEL);
		setCreateIconDescriptor(ModelerIconsCommon.RELATION_MODEL_CREATE);
		setDeleteIconDescriptor(ModelerIconsCommon.RELATION_MODEL_DELETE);
		setCreateLinkIconDescriptor(ModelerIconsCommon.RELATION_MODEL_LINK_CREATE);
		setDeleteLinkIconDescriptor(ModelerIconsCommon.RELATION_MODEL_LINK_DELETE);
		setDefaultSorting(
				Sort.create(IRelationModel.SOURCE_ENTITY_MODEL_ID_PROPERTY),
				Sort.create(IRelationModel.DESTINATION_ORDER_PROPERTY));

		addIdProperty();

		IBeanPropertyBluePrint propertyBp = addProperty(IRelationModel.NAME_PROPERTY);
		propertyBp.setLabel(getMessage("name.label"));
		propertyBp.setDescription(getMessage("name.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IRelationModel.LABEL_PROPERTY);
		propertyBp.setLabel(getMessage("label.label"));
		propertyBp.setDescription(getMessage("label.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IRelationModel.INVERSE_PROPERTY);
		propertyBp.setLabel(getMessage("labelInverse.label"));
		propertyBp.setDescription(getMessage("labelInverse.description"));

		propertyBp = addProperty(IRelationModel.SYMMETRIC_PROPERTY);
		propertyBp.setLabel(getMessage("symmetric.label"));
		propertyBp.setDescription(getMessage("symmetric.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Boolean.FALSE);

		propertyBp = addProperty(IRelationModel.SOURCE_ENTITY_MODEL_ID_PROPERTY);
		propertyBp.setLabel(getMessage("sourceEntityModel.label"));
		propertyBp.setDescription(getMessage("sourceEntityModel.description"));
		propertyBp.setMandatory(true);
		propertyBp.setLookUpValueRange(LookUpIds.ENTITY_MODELS);

		propertyBp = addProperty(IRelationModel.SOURCE_ORDER_PROPERTY);
		propertyBp.setLabel(getMessage("sourceOrder.label"));
		propertyBp.setDescription(getMessage("sourceOrder.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Integer.valueOf(0));

		propertyBp = addProperty(IRelationModel.SOURCE_CARDINALITY_PROPERTY);
		propertyBp.setLabel(getMessage("sourceCardinality.label"));
		propertyBp.setDescription(getMessage("sourceCardinality.description"));
		propertyBp.setMandatory(true);
		propertyBp.setValueRange(CardinalityLookUpService.valueRange());
		propertyBp.setDefaultValue(Cardinality.GREATER_OR_EQUAL_ZERO);

		propertyBp = addProperty(IRelationModel.DESTINATION_ENTITY_MODEL_ID_PROPERTY);
		propertyBp.setLabel(getMessage("destinationEntityModel.label"));
		propertyBp.setDescription(getMessage("destinationEntityModel.description"));
		propertyBp.setMandatory(true);
		propertyBp.setLookUpValueRange(LookUpIds.ENTITY_MODELS);

		propertyBp = addProperty(IRelationModel.DESTINATION_ORDER_PROPERTY);
		propertyBp.setLabel(getMessage("destinationOrder.label"));
		propertyBp.setDescription(getMessage("destinationOrder.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Integer.valueOf(0));

		propertyBp = addProperty(IRelationModel.DESTINATION_CARDINALITY_PROPERTY);
		propertyBp.setLabel(getMessage("destinationCardinality.label"));
		propertyBp.setDescription(getMessage("destinationCardinality.description"));
		propertyBp.setMandatory(true);
		propertyBp.setValueRange(CardinalityLookUpService.valueRange());
		propertyBp.setDefaultValue(Cardinality.GREATER_OR_EQUAL_ZERO);

		addVersionProperty();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("RelationModelDtoDescriptorBuilder." + keySuffix);
	}
}
