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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jowidgets.api.command.EnabledState;
import org.jowidgets.api.command.IAction;
import org.jowidgets.api.command.IEnabledChecker;
import org.jowidgets.api.command.IEnabledState;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.service.IExecutorService;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.bean.IBeanSelectionEvent;
import org.jowidgets.cap.ui.api.bean.IBeanSelectionListener;
import org.jowidgets.cap.ui.api.command.IExecutorActionBuilder;
import org.jowidgets.cap.ui.api.execution.BeanSelectionPolicy;
import org.jowidgets.cap.ui.api.filter.IUiFilter;
import org.jowidgets.cap.ui.api.filter.IUiFilterTools;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.cap.ui.tools.execution.ReloadDataModelExecutionInterceptor;
import org.jowidgets.cap.ui.tools.model.BeanListModelListenerAdapter;
import org.jowidgets.common.types.Modifier;
import org.jowidgets.common.types.VirtualKey;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.common.bean.IPropertyModel;
import org.jowidgets.modeler.ui.icons.ModelerIcons;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.tools.command.ActionWrapper;
import org.jowidgets.util.Assert;
import org.jowidgets.util.event.ChangeObservable;
import org.jowidgets.util.event.IChangeListener;

public final class PropertyModelMoveDownAction extends ActionWrapper {

	private static final IMessage AT_LAST_POSITION = Messages.getMessage("PropertyModelMoveDownAction.alreadyAtLastPosition");
	private static final IMessage MUST_NOT_BE_FILTERED = Messages.getMessage("PropertyModelMoveDownAction.mustNotBeFiltered");

	public PropertyModelMoveDownAction(
		final IBeanTableModel<IPropertyModel> model,
		final IServiceId<IExecutorService<Void>> excecuterServiceId) {
		super(create(model, excecuterServiceId));
	}

	public static IAction create(
		final IBeanTableModel<IPropertyModel> model,
		final IServiceId<IExecutorService<Void>> excecuterServiceId) {
		final IExecutorActionBuilder<IPropertyModel, Void> builder = CapUiToolkit.actionFactory().executorActionBuilder(model);
		builder.setText(Messages.getString("PropertyModelMoveDownAction.label"));
		builder.setToolTipText(Messages.getString("PropertyModelMoveDownAction.tooltip"));
		builder.setIcon(ModelerIcons.ARROW_DOWN);
		builder.setAccelerator(VirtualKey.D, Modifier.SHIFT);
		builder.setSelectionPolicy(BeanSelectionPolicy.MULTI_SELECTION);
		builder.setExecutor(excecuterServiceId);
		builder.addExecutionInterceptor(new ReloadDataModelExecutionInterceptor<List<IBeanDto>>(model));
		builder.addEnabledChecker(new PropertyModelMoveDownEnabledChecker(model));
		return builder.build();
	}

	private static class PropertyModelMoveDownEnabledChecker extends ChangeObservable implements IEnabledChecker {

		private final IBeanTableModel<IPropertyModel> model;
		private final Map<Long, Integer> maxValues;

		PropertyModelMoveDownEnabledChecker(final IBeanTableModel<IPropertyModel> model) {
			Assert.paramNotNull(model, "model");
			this.model = model;
			this.maxValues = new HashMap<Long, Integer>();
			calculateMaxValues();
			model.addBeanListModelListener(new BeanListModelListenerAdapter<IPropertyModel>() {
				@Override
				public void beansChanged() {
					calculateMaxValues();
					fireChangedEvent();
				}
			});

			model.addBeanSelectionListener(new IBeanSelectionListener<IPropertyModel>() {
				@Override
				public void selectionChanged(final IBeanSelectionEvent<IPropertyModel> selectionEvent) {
					fireChangedEvent();
				}
			});

			model.addFilterChangeListener(new IChangeListener() {
				@Override
				public void changed() {
					fireChangedEvent();
				}
			});
		}

		private void calculateMaxValues() {
			maxValues.clear();
			for (int i = 0; i < model.getSize(); i++) {
				final IPropertyModel property = model.getBean(i).getBean();
				final Long parentModelId = property.getParentModelId();
				final Integer maxValue = maxValues.get(parentModelId);
				final Integer order = property.getOrder();
				if (order != null) {
					if (maxValue == null || maxValue.intValue() < order.intValue()) {
						maxValues.put(parentModelId, order);
					}
				}
			}
		}

		@Override
		public IEnabledState getEnabledState() {
			if (!isFilterValid(IBeanTableModel.UI_FILTER_ID) || !isFilterValid(IBeanTableModel.UI_SEARCH_FILTER_ID)) {
				return EnabledState.disabled(MUST_NOT_BE_FILTERED.get());
			}

			for (final IBeanProxy<IPropertyModel> propertyProxy : model.getSelectedBeans()) {
				final IPropertyModel property = propertyProxy.getBean();
				final Integer order = property.getOrder();
				if (order != null) {
					final Integer maxValue = maxValues.get(property.getParentModelId());
					if (maxValue != null && order.intValue() >= maxValue.intValue()) {
						return EnabledState.disabled(AT_LAST_POSITION.get());
					}
				}
			}
			return EnabledState.ENABLED;
		}

		private boolean isFilterValid(final String filterId) {
			final IUiFilter filter = model.getFilter(filterId);
			if (filter != null) {
				final IUiFilterTools filterTools = CapUiToolkit.filterToolkit().filterTools();
				if (filterTools.hasPropertyFilters(filter, IPropertyModel.PARENT_MODEL_ID_PROPERTY)) {
					return false;
				}
			}
			return true;
		}

	}
}
