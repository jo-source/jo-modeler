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

package org.jowidgets.modeler.ui.action;

import org.jowidgets.api.command.IAction;
import org.jowidgets.cap.common.api.service.IExecutorService;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.command.IExecutorActionBuilder;
import org.jowidgets.cap.ui.api.execution.BeanSelectionPolicy;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.cap.ui.tools.execution.ReloadDataModelExecutionInterceptor;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.modeler.common.bean.IPropertyModel;
import org.jowidgets.modeler.ui.icons.ModelerIcons;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.tools.command.ActionWrapper;

public final class PropertyModelMoveUpAction extends ActionWrapper {

	public PropertyModelMoveUpAction(
		final IBeanTableModel<IPropertyModel> model,
		final IServiceId<IExecutorService<Void>> excecuterServiceId) {
		super(create(model, excecuterServiceId));
	}

	public static IAction create(
		final IBeanTableModel<IPropertyModel> model,
		final IServiceId<IExecutorService<Void>> excecuterServiceId) {
		final IExecutorActionBuilder<IPropertyModel, Void> builder = CapUiToolkit.actionFactory().executorActionBuilder(model);
		builder.setText(Messages.getString("PropertyModelMoveUpAction.label"));
		builder.setToolTipText(Messages.getString("PropertyModelMoveUpAction.tooltip"));
		builder.setIcon(ModelerIcons.ARROW_UP);
		builder.setAccelerator(VirtualKey.U, Modifier.SHIFT);
		builder.setSelectionPolicy(BeanSelectionPolicy.MULTI_SELECTION);
		builder.setExecutor(excecuterServiceId);
		builder.addExecutionInterceptor(new ReloadDataModelExecutionInterceptor(model));
		return builder.build();
	}
}
