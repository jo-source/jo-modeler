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

import org.jowidgets.cap.common.api.service.IBeanServicesProvider;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.security.common.api.AuthorizationChecker;
import org.jowidgets.cap.security.common.api.ISecureObject;
import org.jowidgets.cap.ui.api.workbench.CapWorkbenchToolkit;
import org.jowidgets.cap.ui.api.workbench.IEntityComponentNodesFactory;
import org.jowidgets.common.image.IImageConstant;
import org.jowidgets.modeler.common.entity.EntityIds;
import org.jowidgets.modeler.ui.icons.ModelerIcons;
import org.jowidgets.modeler.ui.messages.ModelerMessages;
import org.jowidgets.service.api.ServiceProvider;
import org.jowidgets.workbench.toolkit.api.IComponentNodeModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModel;
import org.jowidgets.workbench.toolkit.api.IWorkbenchApplicationModelBuilder;
import org.jowidgets.workbench.tools.WorkbenchApplicationModelBuilder;

public final class ModelerApplicationFactory {

	private ModelerApplicationFactory() {}

	public static IWorkbenchApplicationModel create() {
		final IWorkbenchApplicationModelBuilder builder = new WorkbenchApplicationModelBuilder();

		builder.setId(ModelerApplicationFactory.class.getName());
		builder.setLabel(ModelerMessages.MODELER_LABEL.get());
		createComponentTree(builder);

		return builder.build();
	}

	private static void createComponentTree(final IWorkbenchApplicationModelBuilder model) {
		addEntityComponent(model, EntityIds.ENTITY_MODEL, ModelerIcons.ENTITY_MODEL);
		addEntityComponent(model, EntityIds.RELATION_MODEL, ModelerIcons.RELATION_MODEL);
		addEntityComponent(model, EntityIds.ENTITY_PROPERTY_MODEL, ModelerIcons.PROPERTY_MODEL);
		addEntityComponent(model, EntityIds.LOOK_UP, ModelerIcons.LOOK_UP);
		addEntityComponent(model, EntityIds.ICON_SET, ModelerIcons.ICON_SET);
		addEntityComponent(model, EntityIds.ICON, ModelerIcons.ICON);
	}

	private static void addEntityComponent(
		final IWorkbenchApplicationModelBuilder parent,
		final Object entityId,
		final IImageConstant icon) {
		final IEntityComponentNodesFactory nodesFactory = CapWorkbenchToolkit.entityComponentNodesFactory();
		if (hasReaderServiceAuthorization(entityId)) {
			final IComponentNodeModel node = nodesFactory.createNode(entityId);
			node.setIcon(icon);
			parent.addChild(node);
		}
	}

	private static boolean hasReaderServiceAuthorization(final Object entityId) {
		final Object authorization = getReaderServiceAuthorization(entityId);
		if (authorization != null) {
			return AuthorizationChecker.hasAuthorization(authorization);
		}
		else {
			return true;
		}
	}

	private static Object getReaderServiceAuthorization(final Object entityId) {
		final IEntityService entityService = ServiceProvider.getService(IEntityService.ID);
		if (entityService != null) {
			final IBeanServicesProvider beanServices = entityService.getBeanServices(entityId);
			if (beanServices != null) {
				final IReaderService<Void> readerService = beanServices.readerService();
				if (readerService instanceof ISecureObject<?>) {
					return ((ISecureObject<?>) readerService).getAuthorization();
				}
			}
		}
		return null;
	}

}
