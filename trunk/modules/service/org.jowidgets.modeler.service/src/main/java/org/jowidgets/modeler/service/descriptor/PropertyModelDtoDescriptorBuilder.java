/*
 * Copyright (c) 2011, grossmann
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
import org.jowidgets.modeler.common.bean.IPropertyModel;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.modeler.common.lookup.LookUpIds;
import org.jowidgets.modeler.service.lookup.ValueTypeLookUpService;
import org.jowidgets.util.Assert;

public final class PropertyModelDtoDescriptorBuilder extends AbstractDtoDescriptorBuilder {

	public PropertyModelDtoDescriptorBuilder() {
		super(IPropertyModel.class);

		setLabelSingular(getMessage("label.singular"));
		setLabelPlural(getMessage("label.plural"));
		setRenderingPattern("$" + IPropertyModel.NAME_PROPERTY + "$");
		setDefaultSorting(Sort.create(IPropertyModel.NAME_PROPERTY));

		addIdProperty();

		IBeanPropertyBluePrint propertyBp = addProperty(IPropertyModel.NAME_PROPERTY);
		propertyBp.setLabel(getMessage("name.label"));
		propertyBp.setDescription(getMessage("name.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPropertyModel.LABEL_PROPERTY);
		propertyBp.setLabel(getMessage("label.label"));
		propertyBp.setDescription(getMessage("label.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IPropertyModel.LABEL_LONG_PROPERTY);
		propertyBp.setLabel(getMessage("labelLong.label"));
		propertyBp.setDescription(getMessage("labelLong.description"));

		propertyBp = addProperty(IPropertyModel.DESCRIPTION_PROPERTY);
		propertyBp.setLabel(getMessage("description.label"));
		propertyBp.setDescription(getMessage("description.description"));

		propertyBp = addProperty(IPropertyModel.VALUE_TYPE_PROPERTY);
		propertyBp.setLabel(getMessage("elementValueType.label"));
		propertyBp.setDescription(getMessage("elementValueType.description"));
		propertyBp.setMandatory(true);
		propertyBp.setLookUpValueRange(LookUpIds.VALUE_TYPES);
		propertyBp.setDefaultValue(ValueTypeLookUpService.STRING_KEY);

		propertyBp = addProperty(IPropertyModel.MANDATORY_PROPERTY);
		propertyBp.setLabel(getMessage("mandatory.label"));
		propertyBp.setDescription(getMessage("madatory.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Boolean.FALSE);

		propertyBp = addProperty(IPropertyModel.EDITABLE_PROPERTY);
		propertyBp.setLabel(getMessage("editable.label"));
		propertyBp.setDescription(getMessage("editable.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Boolean.TRUE);

		propertyBp = addProperty(IPropertyModel.SEARCHABLE_PROPERTY);
		propertyBp.setLabel(getMessage("searchable.label"));
		propertyBp.setDescription(getMessage("searchable.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Boolean.TRUE);

		propertyBp = addProperty(IPropertyModel.VISIBLE_PROPERTY);
		propertyBp.setLabel(getMessage("visible.label"));
		propertyBp.setDescription(getMessage("visible.description"));
		propertyBp.setMandatory(true);
		propertyBp.setDefaultValue(Boolean.TRUE);

		propertyBp = addProperty(IPropertyModel.TABLE_WIDTH_PROPERTY);
		propertyBp.setLabel(getMessage("tableWidth.label"));
		propertyBp.setDescription(getMessage("tableWidth.description"));

		addVersionProperty();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("PropertyModelDtoDescriptorBuilder." + keySuffix);
	}
}
