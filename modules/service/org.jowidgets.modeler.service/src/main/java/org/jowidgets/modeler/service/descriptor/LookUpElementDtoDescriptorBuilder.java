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
import org.jowidgets.modeler.common.bean.ILookUpElement;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.modeler.common.icons.ModelerIconsCommon;
import org.jowidgets.util.Assert;

public class LookUpElementDtoDescriptorBuilder extends AbstractDtoDescriptorBuilder {

	public LookUpElementDtoDescriptorBuilder() {
		this("label.singular", "label.plural");
	}

	public LookUpElementDtoDescriptorBuilder(final String labelSingularKey, final String labelPluralKey) {
		super(ILookUpElement.class);

		setLabelSingular(getMessage(labelSingularKey));
		setLabelPlural(getMessage(labelPluralKey));
		setIconDescriptor(ModelerIconsCommon.LOOK_UP_ELEMENT);
		setCreateIconDescriptor(ModelerIconsCommon.LOOK_UP_ELEMENT_CREATE);
		setDeleteIconDescriptor(ModelerIconsCommon.LOOK_UP_ELEMENT_DELETE);
		setCreateLinkIconDescriptor(ModelerIconsCommon.LOOK_UP_ELEMENT_LINK_CREATE);
		setDeleteLinkIconDescriptor(ModelerIconsCommon.LOOK_UP_ELEMENT_LINK_DELETE);
		setDefaultSorting(Sort.create(ILookUpElement.KEY_PROPERTY));

		addIdProperty();

		IBeanPropertyBluePrint propertyBp = addProperty(ILookUpElement.KEY_PROPERTY);
		propertyBp.setLabel(getMessage("key.label"));
		propertyBp.setDescription(getMessage("key.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(ILookUpElement.LABEL_PROPERTY);
		propertyBp.setLabel(getMessage("label.label"));
		propertyBp.setDescription(getMessage("label.description"));

		propertyBp = addProperty(ILookUpElement.LABEL_LONG_PROPERTY);
		propertyBp.setLabel(getMessage("labelLong.label"));
		propertyBp.setDescription(getMessage("labelLong.description"));

		propertyBp = addProperty(ILookUpElement.DESCRIPTION_PROPERTY);
		propertyBp.setLabel(getMessage("description.label"));
		propertyBp.setDescription(getMessage("description.description"));

		propertyBp = addProperty(ILookUpElement.IS_VALID_PROPERTY);
		propertyBp.setLabel(getMessage("isValid.label"));
		propertyBp.setDescription(getMessage("isValid.description"));
		propertyBp.setDefaultValue(Boolean.TRUE);
		propertyBp.setMandatory(true);

		addVersionProperty();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("LookUpEntryDtoDescriptorBuilder." + keySuffix);
	}
}
