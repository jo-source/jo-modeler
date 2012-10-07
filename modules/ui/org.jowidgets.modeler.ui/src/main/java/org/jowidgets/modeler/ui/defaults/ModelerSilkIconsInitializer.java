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

package org.jowidgets.modeler.ui.defaults;

import org.jowidgets.addons.icons.silkicons.SilkIcons;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.common.image.IImageRegistry;
import org.jowidgets.modeler.ui.icons.ModelerIcons;
import org.jowidgets.useradmin.ui.defaults.UserAdminSilkIconsInitializer;

public final class ModelerSilkIconsInitializer {

	private ModelerSilkIconsInitializer() {}

	public static void initialize() {
		UserAdminSilkIconsInitializer.initialize();

		final IImageRegistry registry = Toolkit.getImageRegistry();

		registry.registerImageConstant(ModelerIcons.MODELER_ICON, SilkIcons.TABLE);
		registry.registerImageConstant(ModelerIcons.ENTITY_MODEL, SilkIcons.TABLE);
		registry.registerImageConstant(ModelerIcons.PROPERTY_MODEL, SilkIcons.TEXTFIELD);
		registry.registerImageConstant(ModelerIcons.RELATION_MODEL, SilkIcons.LINK);
		registry.registerImageConstant(ModelerIcons.LOOK_UP, SilkIcons.FIND);
		registry.registerImageConstant(ModelerIcons.ARROW_UP, SilkIcons.ARROW_UP);
		registry.registerImageConstant(ModelerIcons.ARROW_DOWN, SilkIcons.ARROW_DOWN);
	}

}
