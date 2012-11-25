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

import org.jowidgets.cap.common.api.bean.IBeanPropertyBluePrint;
import org.jowidgets.cap.common.api.sort.Sort;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.util.Assert;

public class EntityModelDtoDescriptorBuilder extends AbstractDtoDescriptorBuilder {

	public EntityModelDtoDescriptorBuilder() {
		this("label.singular", "label.plural");
	}

	public EntityModelDtoDescriptorBuilder(final String labelSingularKey, final String labelPluralKey) {
		super(IEntityModel.class);

		setLabelSingular(getMessage(labelSingularKey));
		setLabelPlural(getMessage(labelPluralKey));
		setRenderingPattern("$" + IEntityModel.LABEL_SINGULAR_PROPERTY + "$");
		setDefaultSorting(Sort.create(IEntityModel.NAME_PROPERTY));

		addIdProperty();

		IBeanPropertyBluePrint propertyBp = addProperty(IEntityModel.NAME_PROPERTY);
		propertyBp.setLabel(getMessage("name.label"));
		propertyBp.setDescription(getMessage("name.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IEntityModel.LABEL_SINGULAR_PROPERTY);
		propertyBp.setLabel(getMessage("labelSingular.label"));
		propertyBp.setDescription(getMessage("labelSingular.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IEntityModel.LABEL_PLURAL_PROPERTY);
		propertyBp.setLabel(getMessage("labelPlural.label"));
		propertyBp.setDescription(getMessage("labelPlural.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IEntityModel.RENDERING_PATTERN_PROPERTY);
		propertyBp.setLabel(getMessage("renderingPattern.label"));
		propertyBp.setDescription(getMessage("renderingPattern.description"));

		propertyBp = addProperty(IEntityModel.ICON_DESCRIPTOR_PROPERTY);
		propertyBp.setLabel(getMessage("icon.label"));
		propertyBp.setDescription(getMessage("icon.description"));

		propertyBp = addProperty(IEntityModel.CREATE_ICON_DESCRIPTOR_PROPERTY);
		propertyBp.setLabel(getMessage("createIcon.label"));
		propertyBp.setDescription(getMessage("createIcon.description"));

		propertyBp = addProperty(IEntityModel.DELETE_ICON_DESCRIPTOR_PROPERTY);
		propertyBp.setLabel(getMessage("deleteIcon.label"));
		propertyBp.setDescription(getMessage("deleteIcon.description"));

		propertyBp = addProperty(IEntityModel.PROPERTIES_NAMES_PROPERTY);
		propertyBp.setLabel(getMessage("propertiesNames.label"));
		propertyBp.setDescription(getMessage("propertiesNames.description"));

		addVersionProperty();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("EntityModelDtoDescriptorBuilder." + keySuffix);
	}
}
