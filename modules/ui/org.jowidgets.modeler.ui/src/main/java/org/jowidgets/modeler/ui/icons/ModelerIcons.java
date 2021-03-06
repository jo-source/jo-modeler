/*
 * Copyright (c) 2012, Michael Grossmann
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * * Neither the name of the jo-widgets.org nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT
 * LIABILITY, OR TORT(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY
 * OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH
 * DAMAGE.
 */
package org.jowidgets.modeler.ui.icons;

import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.modeler.common.icons.ModelerIconsCommon;

public enum ModelerIcons implements IImageConstant {

	MODELER_ICON,

	ENTITY_MODEL,
	ENTITY_MODEL_CREATE,
	ENTITY_MODEL_DELETE,
	ENTITY_MODEL_LINK_CREATE,
	ENTITY_MODEL_LINK_DELETE,

	PROPERTY_MODEL,
	PROPERTY_MODEL_CREATE,
	PROPERTY_MODEL_DELETE,
	PROPERTY_MODEL_LINK_CREATE,
	PROPERTY_MODEL_LINK_DELETE,

	RELATION_MODEL,
	RELATION_MODEL_CREATE,
	RELATION_MODEL_DELETE,
	RELATION_MODEL_LINK_CREATE,
	RELATION_MODEL_LINK_DELETE,

	LOOK_UP,
	LOOK_UP_CREATE,
	LOOK_UP_DELETE,
	LOOK_UP_LINK_CREATE,
	LOOK_UP_LINK_DELETE,

	LOOK_UP_ELEMENT,
	LOOK_UP_ELEMENT_CREATE,
	LOOK_UP_ELEMENT_DELETE,
	LOOK_UP_ELEMENT_LINK_CREATE,
	LOOK_UP_ELEMENT_LINK_DELETE,

	ICON,
	ICON_CREATE,
	ICON_DELETE,
	ICON_LINK_CREATE,
	ICON_LINK_DELETE,
	ICON_ADD,

	ICON_SET,
	ICON_SET_CREATE,
	ICON_SET_DELETE,
	ICON_SET_LINK_CREATE,
	ICON_SET_LINK_DELETE,

	ARROW_UP,
	ARROW_DOWN,
	DELETE,
	LINK;
	public static ModelerIcons getIcon(final ModelerIconsCommon commonConstant) {
		return valueOf(commonConstant.name());
	}

}
