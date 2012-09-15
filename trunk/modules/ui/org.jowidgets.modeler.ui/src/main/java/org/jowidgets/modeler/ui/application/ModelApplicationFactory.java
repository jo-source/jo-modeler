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

package org.jowidgets.modeler.ui.application;

import org.jowidgets.cap.common.api.service.IEntityApplicationService;
import org.jowidgets.cap.ui.api.workbench.CapWorkbenchToolkit;
import org.jowidgets.cap.ui.api.workbench.IEntityComponentNodesFactory;
import org.jowidgets.service.api.ServiceProvider;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.tools.WorkbenchApplicationModelBuilder;

public final class ModelApplicationFactory {

	private ModelApplicationFactory() {}

	public static IWorkbenchApplicationModel create() {
		final IEntityApplicationService appService = ServiceProvider.getService(IEntityApplicationService.ID);
		if (appService != null) {
			final IWorkbenchApplicationModelBuilder builder = new WorkbenchApplicationModelBuilder();
			builder.setId(ModelApplicationFactory.class.getName());
			builder.setLabel(appService.getLabel().get());
			builder.setTooltip(appService.getDescription().get());

			final IEntityComponentNodesFactory nodesFactory = CapWorkbenchToolkit.entityComponentNodesFactory();
			for (final IComponentNodeModel nodeModel : nodesFactory.createNodes(appService)) {
				builder.addChild(nodeModel);
			}
			return builder.build();
		}
		else {
			return null;
		}
	}

}
