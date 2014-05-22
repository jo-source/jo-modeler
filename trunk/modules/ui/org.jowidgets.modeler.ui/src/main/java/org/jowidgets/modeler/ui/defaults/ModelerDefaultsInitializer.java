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

import org.jowidgets.api.types.AutoPackPolicy;
import org.jowidgets.api.widgets.blueprint.builder.IComboBoxSelectionSetupBuilder;
import org.jowidgets.api.widgets.blueprint.builder.ITableSetupBuilder;
import org.jowidgets.api.widgets.blueprint.defaults.IDefaultInitializer;
import org.jowidgets.cap.ui.api.control.InputControlSupportRegistry;
import org.jowidgets.cap.ui.api.image.ImageResolver;
import org.jowidgets.cap.ui.api.types.RelationRenderingPolicy;
import org.jowidgets.cap.ui.api.widgets.IBeanFormBluePrint;
import org.jowidgets.cap.ui.api.widgets.IBeanRelationTreeBluePrint;
import org.jowidgets.cap.ui.api.widgets.IBeanRelationTreeDetailBluePrint;
import org.jowidgets.cap.ui.api.widgets.IBeanTableBluePrint;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.modeler.common.icons.ModelerIconsCommon;
import org.jowidgets.modeler.ui.icons.IconDescriptorControlProvider;
import org.jowidgets.modeler.ui.icons.IconDescriptorResolver;
import org.jowidgets.modeler.ui.icons.ModelerIconsCommonResolver;
import org.jowidgets.tools.widgets.blueprint.BPF;

public final class ModelerDefaultsInitializer {

	private ModelerDefaultsInitializer() {}

	public static void initialize(final boolean autoCompletionCombos) {

		ImageResolver.register(IconDescriptor.class, new IconDescriptorResolver());
		ImageResolver.register(ModelerIconsCommon.class, new ModelerIconsCommonResolver());

		InputControlSupportRegistry.setControl(IconDescriptor.class, IconDescriptorControlProvider.create());

		BPF.addDefaultsInitializer(
				IComboBoxSelectionSetupBuilder.class,
				new IDefaultInitializer<IComboBoxSelectionSetupBuilder<?, ?>>() {
					@Override
					public void initialize(final IComboBoxSelectionSetupBuilder<?, ?> setupBuilder) {
						setupBuilder.setAutoCompletion(autoCompletionCombos);
					}
				});

		BPF.addDefaultsInitializer(IBeanTableBluePrint.class, new IDefaultInitializer<IBeanTableBluePrint<?>>() {
			@Override
			public void initialize(final IBeanTableBluePrint<?> bluePrint) {
				bluePrint.setAutoPackPolicy(AutoPackPolicy.ONCE);
				bluePrint.setSearchFilterToolbarVisible(true);
				bluePrint.setDefaultCopyAction(true);
				bluePrint.setDefaultPasteAction(true);
				bluePrint.setEditable(true);
			}
		});

		BPF.addDefaultsInitializer(IBeanFormBluePrint.class, new IDefaultInitializer<IBeanFormBluePrint<?>>() {
			@Override
			public void initialize(final IBeanFormBluePrint<?> builder) {
				builder.setMaxWidthDefault(800);
			}
		});

		BPF.addDefaultsInitializer(ITableSetupBuilder.class, new IDefaultInitializer<ITableSetupBuilder<?>>() {
			@Override
			public void initialize(final ITableSetupBuilder<?> setupBuilder) {
				setupBuilder.setEditable(true);
			}
		});

		BPF.addDefaultsInitializer(
				IBeanRelationTreeDetailBluePrint.class,
				new IDefaultInitializer<IBeanRelationTreeDetailBluePrint<?>>() {
					@Override
					public void initialize(final IBeanRelationTreeDetailBluePrint<?> bluePrint) {
						bluePrint.setDefaultCopyAction(true);
						bluePrint.setDefaultLinkPasteAction(true);
					}
				});

		BPF.addDefaultsInitializer(IBeanRelationTreeBluePrint.class, new IDefaultInitializer<IBeanRelationTreeBluePrint<?>>() {
			@Override
			public void initialize(final IBeanRelationTreeBluePrint<?> bluePrint) {
				bluePrint.setRelationRenderingPolicy(RelationRenderingPolicy.GREY_EMPTY_RELATIONS);
				bluePrint.setDefaultCopyAction(true);
				bluePrint.setDefaultLinkPasteAction(true);
			}
		});
	}
}
