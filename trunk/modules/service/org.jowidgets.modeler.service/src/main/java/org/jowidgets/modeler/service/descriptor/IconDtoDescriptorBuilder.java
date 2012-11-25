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
import org.jowidgets.modeler.common.bean.IIcon;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.util.Assert;

public class IconDtoDescriptorBuilder extends AbstractDtoDescriptorBuilder {

	public IconDtoDescriptorBuilder() {
		this("label.singular", "label.plural");
	}

	public IconDtoDescriptorBuilder(final String labelSingularKey, final String labelPluralKey) {
		super(IIcon.class);

		setLabelSingular(getMessage(labelSingularKey));
		setLabelPlural(getMessage(labelPluralKey));
		setRenderingPattern("$" + IIcon.KEY_PROPERTY + "$");
		setDefaultSorting(Sort.create(IIcon.ICON_SET_LABEL_PROPERTY), Sort.create(IIcon.KEY_PROPERTY));

		addIdProperty();

		IBeanPropertyBluePrint propertyBp = addProperty(IIcon.ICON_SET_LABEL_PROPERTY);
		propertyBp.setLabel(getMessage("iconSetLabel.label"));
		propertyBp.setDescription(getMessage("iconSetLabel.description"));
		propertyBp.setSearchable(false);

		propertyBp = addProperty(IIcon.KEY_PROPERTY);
		propertyBp.setLabel(getMessage("key.label"));
		propertyBp.setDescription(getMessage("key.description"));
		propertyBp.setMandatory(true);

		propertyBp = addProperty(IIcon.LABEL_PROPERTY);
		propertyBp.setLabel(getMessage("label.label"));
		propertyBp.setDescription(getMessage("label.description"));

		propertyBp = addProperty(IIcon.DESCRIPTOR_PROPERTY);
		propertyBp.setLabel(getMessage("icon.label"));
		propertyBp.setSortable(false).setFilterable(false).setSearchable(false);

		propertyBp = addProperty(IIcon.SIZE_PROPERTY);
		propertyBp.setLabel(getMessage("size.label"));
		propertyBp.setDescription(getMessage("size.description"));
		propertyBp.setSortable(false).setFilterable(false).setSearchable(false);

		addVersionProperty();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("IconDtoDescriptorBuilder." + keySuffix);
	}
}
