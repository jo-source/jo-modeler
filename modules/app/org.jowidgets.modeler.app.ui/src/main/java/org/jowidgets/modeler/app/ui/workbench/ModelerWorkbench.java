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

package org.jowidgets.modeler.app.ui.workbench;

import org.jowidgets.addons.icons.silkicons.SilkIconsSubstitude;
import org.jowidgets.cap.ui.tools.workbench.CapWorkbenchModelBuilder;
import org.jowidgets.modeler.ui.action.UserAdminPasswordChangeAction;
import org.jowidgets.modeler.ui.application.ModelApplicationFactory;
import org.jowidgets.modeler.ui.application.ModelerApplicationFactory;
import org.jowidgets.modeler.ui.defaults.ModelerDefaultsInitializer;
import org.jowidgets.modeler.ui.defaults.ModelerSilkIconsInitializer;
import org.jowidgets.modeler.ui.icons.ModelerIcons;
import org.jowidgets.modeler.ui.lookup.LookupInitializer;
import org.jowidgets.modeler.ui.messages.ModelerMessages;
import org.jowidgets.modeler.ui.workbench.WorkbenchSettingsMenu;
import org.jowidgets.workbench.api.IWorkbench;
import org.jowidgets.workbench.api.IWorkbenchContext;
import org.jowidgets.workbench.api.IWorkbenchFactory;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchInitializeCallback;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchModelBuilder;
import org.jowidgets.workbench.toolkit.api.WorkbenchPartFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

public final class ModelerWorkbench implements IWorkbenchFactory {

	private final boolean rwt;
	private final boolean autoCompletionCombos;

	public ModelerWorkbench() {
		this(false, true);
	}

	public ModelerWorkbench(final boolean rwt, final boolean autoCompletionCombos) {
		this.rwt = rwt;
		this.autoCompletionCombos = autoCompletionCombos;
	}

	@Override
	public IWorkbench create() {

		SLF4JBridgeHandler.removeHandlersForRootLogger();
		SLF4JBridgeHandler.install();

		SilkIconsSubstitude.substitude();

		ModelerSilkIconsInitializer.initialize();
		ModelerDefaultsInitializer.initialize(autoCompletionCombos);

		LookupInitializer.initializeLookupsAsync();

		final IWorkbenchModelBuilder builder = new CapWorkbenchModelBuilder();

		builder.setIcon(ModelerIcons.MODELER_ICON);
		builder.setLabel(ModelerMessages.MODELER_LABEL.get());
		builder.setInitialMaximized(rwt);
		builder.setDecorated(!rwt);

		builder.addInitializeCallback(new IWorkbenchInitializeCallback() {
			@Override
			public void onContextInitialize(final IWorkbenchModel model, final IWorkbenchContext context) {
				model.getMenuBar().addMenu(new WorkbenchSettingsMenu());
				model.getToolBar().addSeparator();
				model.getToolBar().addAction(new UserAdminPasswordChangeAction());

				final IWorkbenchApplicationModel application = ModelApplicationFactory.create();
				if (application != null) {
					model.addApplication(application);
				}
				final IWorkbenchApplicationModel modelerAppp = ModelerApplicationFactory.create();
				if (modelerAppp.getChildrenCount() > 0) {
					model.addApplication(modelerAppp);
				}
			}
		});

		return WorkbenchPartFactory.workbench(builder.build());
	}
}
