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

package org.jowidgets.modeler.ui.icons;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import org.jowidgets.api.color.Colors;
import org.jowidgets.api.controller.IDisposeListener;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.IInputField;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.common.api.execution.IResultCallback;
import org.jowidgets.cap.common.api.filter.ArithmeticFilter;
import org.jowidgets.cap.common.api.filter.ArithmeticOperator;
import org.jowidgets.cap.common.api.filter.BooleanFilter;
import org.jowidgets.cap.common.api.filter.BooleanOperator;
import org.jowidgets.cap.common.api.filter.IBooleanFilterBuilder;
import org.jowidgets.cap.common.api.filter.IFilter;
import org.jowidgets.cap.common.api.service.IBeanServicesProvider;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.common.api.service.IReaderService;
import org.jowidgets.cap.common.api.sort.ISort;
import org.jowidgets.cap.common.api.sort.Sort;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.execution.IExecutionTask;
import org.jowidgets.cap.ui.api.widgets.ICapApiBluePrintFactory;
import org.jowidgets.cap.ui.tools.execution.AbstractUiResultCallback;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.controller.IInputListener;
import org.jowidgets.common.widgets.controller.IMouseButtonEvent;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.modeler.common.entity.EntityIds;
import org.jowidgets.modeler.common.lookup.LookUpIds;
import org.jowidgets.service.api.ServiceProvider;
import org.jowidgets.tools.controller.MouseAdapter;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.concurrent.DaemonThreadFactory;

final class IconContentCreator implements IInputContentCreator<IconDescriptor> {

	private static final int LISTENER_DELAY = 500;
	private static final int MAX_PAGE_SIZE = 10000;
	private static final List<ISort> SORTING = createSorting();

	private final IReaderService<Void> readerService;
	private final IInputListener filterInputListener;
	private final IInputListener imediateFilterInputListener;

	private IComboBox<Object> iconSetFilterCmb;
	private IInputField<String> iconFilterField;

	private IIcon currentIcon;
	private IInputField<String> currentIconField;
	private IButton deleteButton;

	private IconCompositeLayout contentLayouter;
	private IComposite content;

	private ScheduledExecutorService executorService;
	private IconsLoader loader;

	private IconDescriptor value;

	public IconContentCreator() {
		final IEntityService entityService = ServiceProvider.getService(IEntityService.ID);
		if (entityService == null) {
			throw new IllegalStateException("No entity service found");
		}
		final IBeanServicesProvider beanServices = entityService.getBeanServices(EntityIds.ICON);
		if (beanServices == null) {
			throw new IllegalStateException("No bean services found for icons");
		}
		this.readerService = beanServices.readerService();
		if (readerService == null) {
			throw new IllegalStateException("No reader service found for icons");
		}
		this.filterInputListener = new FilterInputListener();
		this.imediateFilterInputListener = new ImediateFilterInputListener();
	}

	private static List<ISort> createSorting() {
		final List<ISort> result = new LinkedList<ISort>();
		result.add(Sort.create(org.jowidgets.modeler.common.bean.IIcon.ICON_SET_ID_PROPERTY));
		result.add(Sort.create(org.jowidgets.modeler.common.bean.IIcon.KEY_PROPERTY));
		return result;
	}

	@Override
	public void createContent(final IInputContentContainer container) {
		final ICapApiBluePrintFactory cbpf = CapUiToolkit.bluePrintFactory();

		container.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[]15[]15[grow, 0::]"));
		final IComposite searchBar = container.add(BPF.composite(), "growx, w 0::, wrap");
		searchBar.setLayout(new MigLayoutDescriptor("0[][grow, 0::]15[][grow, 0::]0", "0[][]0"));

		searchBar.add(BPF.textSeparator("Filter").setMarkup(Markup.STRONG), "span 4, growx, w 0::, wrap");
		searchBar.add(BPF.textLabel("Icon set"));
		this.iconSetFilterCmb = searchBar.add(cbpf.lookUpComboBox(LookUpIds.ICONS_SETS), "growx, sg g1");

		searchBar.add(BPF.textLabel("Icon"));
		this.iconFilterField = searchBar.add(BPF.inputFieldString(), "growx, w 0::, sg g1, wrap");

		final IComposite selectedIconBar = container.add(BPF.composite(), "growx, w 0::, wrap");
		selectedIconBar.setLayout(new MigLayoutDescriptor("0[]2[grow, 0::]2[]0", "0[][]0"));

		selectedIconBar.add(BPF.textSeparator("Selected icon").setMarkup(Markup.STRONG), "span 3, growx, w 0::, wrap");
		currentIcon = selectedIconBar.add(BPF.icon(), "h 16!, w 16!");
		currentIconField = selectedIconBar.add(BPF.inputFieldString().setEditable(false), "growx, w 0::");
		deleteButton = selectedIconBar.add(BPF.button().setIcon(ModelerIcons.DELETE), "h 22!, w 22!");
		deleteButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				setCurrentIcon(null);
			}
		});

		this.content = container.add(BPF.scrollComposite().setHorizontalBar(false), "growx, w 0::, growy, h 0::");
		this.contentLayouter = new IconCompositeLayout(content, container);

		iconSetFilterCmb.addInputListener(imediateFilterInputListener);
		iconFilterField.addInputListener(filterInputListener);

		container.addDisposeListener(new IDisposeListener() {
			@Override
			public void onDispose() {
				if (loader != null) {
					loader.cancel();
				}
			}
		});
	}

	@Override
	public void setValue(final IconDescriptor value) {
		iconSetFilterCmb.removeInputListener(imediateFilterInputListener);
		final Object iconSetId;
		if (value != null) {
			iconSetId = value.getIconSetId();
		}
		else {
			final List<Object> elements = iconSetFilterCmb.getElements();
			if (elements != null && elements.size() > 0) {
				iconSetId = elements.iterator().next();
			}
			else {
				iconSetId = null;
			}
		}
		iconSetFilterCmb.setValue(iconSetId);
		setCurrentIcon(value);
		load(true);
		iconSetFilterCmb.addInputListener(imediateFilterInputListener);
	}

	@Override
	public IconDescriptor getValue() {
		return value;
	}

	private void setCurrentIcon(final IconDescriptor value) {
		if (value != null) {
			currentIconField.setValue(value.getIconLabel() + " (" + value.getIconSetLabel() + ")");
			currentIcon.setIcon(new DynamicIcon(value));
		}
		else {
			currentIconField.setValue(null);
			currentIcon.setIcon(null);
		}
		this.value = value;
	}

	private void load(final boolean immediate) {
		if (loader != null) {
			loader.cancel();
		}
		loader = new IconsLoader();
		if (immediate) {
			loader.loadImediate();
		}
		else {
			loader.loadScheduled();
		}
	}

	private void setLoading() {
		content.layoutBegin();
		content.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[]"));
		content.removeAll();
		content.add(BPF.textLabel("Loading icons ..."), "growx, w 0::");
		content.layoutEnd();
	}

	private void setIcons(final List<IBeanDto> icons) {
		content.layoutBegin();
		content.setLayout(contentLayouter);
		content.removeAll();
		for (final IBeanDto iconDto : icons) {
			final IconDescriptor iconDescriptor = (IconDescriptor) iconDto.getValue(org.jowidgets.modeler.common.bean.IIcon.DESCRIPTOR_PROPERTY);
			if (iconDescriptor != null) {
				final IIcon icon = content.add(BPF.icon().setIcon(new DynamicIcon(iconDescriptor)));
				icon.setToolTipText(iconDescriptor.getIconLabel() + " (" + iconDescriptor.getIconSetLabel() + ")");
				icon.addMouseListener(new MouseAdapter() {
					@Override
					public void mousePressed(final IMouseButtonEvent mouseEvent) {
						setCurrentIcon(iconDescriptor);
					}
				});
			}
		}
		content.layoutEnd();
	}

	private void setError(final Throwable exception) {
		content.layoutBegin();
		content.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[]"));
		content.removeAll();
		content.add(BPF.textLabel("Error while loading icons").setForegroundColor(Colors.ERROR), "growx, w 0::");
		content.layoutEnd();
	}

	private ScheduledExecutorService getExecutorService() {
		if (executorService == null) {
			executorService = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
		}
		return executorService;
	}

	private IFilter createFilter() {
		final IFilter iconSetFilter = createIconSetFilter();
		final IFilter iconFilter = createIconFilter();

		if (iconFilter != null && iconSetFilter != null) {
			final IBooleanFilterBuilder booleanFilterBuilder = BooleanFilter.builder();
			booleanFilterBuilder.setOperator(BooleanOperator.AND);
			booleanFilterBuilder.addFilter(iconFilter);
			booleanFilterBuilder.addFilter(iconSetFilter);
			return booleanFilterBuilder.build();
		}
		else if (iconFilter != null) {
			return iconFilter;
		}
		else if (iconSetFilter != null) {
			return iconSetFilter;
		}
		else {
			return null;
		}
	}

	private IFilter createIconSetFilter() {
		final Object iconSet = iconSetFilterCmb.getValue();
		if (!EmptyCheck.isEmpty(iconSet)) {
			return ArithmeticFilter.create(
					org.jowidgets.modeler.common.bean.IIcon.ICON_SET_ID_PROPERTY,
					ArithmeticOperator.EQUAL,
					iconSet);
		}
		else {
			return null;
		}
	}

	private IFilter createIconFilter() {
		final String icon = iconFilterField.getValue();
		if (!EmptyCheck.isEmpty(icon)) {
			return ArithmeticFilter.create(org.jowidgets.modeler.common.bean.IIcon.KEY_PROPERTY, ArithmeticOperator.EQUAL, icon
				+ "*");
		}
		else {
			return null;
		}
	}

	private final class FilterInputListener implements IInputListener {
		@Override
		public void inputChanged() {
			load(false);
		}
	}

	private final class ImediateFilterInputListener implements IInputListener {
		@Override
		public void inputChanged() {
			load(true);
		}
	}

	private final class IconsLoader {

		private final IUiThreadAccess uiThreadAccess;

		private ScheduledFuture<?> schedule;
		private IExecutionTask executionTask;
		private boolean canceled;

		private IconsLoader() {
			this.uiThreadAccess = Toolkit.getUiThreadAccess();
			this.canceled = false;
		}

		private void loadScheduled() {
			final Runnable runnable = new Runnable() {
				@Override
				public void run() {
					uiThreadAccess.invokeLater(new Runnable() {
						@Override
						public void run() {
							schedule = null;
							loadImediate();
						}
					});
				}
			};
			schedule = getExecutorService().schedule(runnable, LISTENER_DELAY, TimeUnit.MILLISECONDS);
		}

		void loadImediate() {
			setLoading();
			final IFilter filter = createFilter();
			this.executionTask = CapUiToolkit.executionTaskFactory().create();
			final IResultCallback<List<IBeanDto>> resultCallback = new AbstractUiResultCallback<List<IBeanDto>>() {
				@Override
				protected void finishedUi(final List<IBeanDto> result) {
					if (!content.isDisposed()) {
						setIcons(result);
					}
				}

				@Override
				protected void exceptionUi(final Throwable exception) {
					if (!content.isDisposed()) {
						setError(exception);
					}
				}
			};
			readerService.read(resultCallback, null, filter, SORTING, 0, MAX_PAGE_SIZE, null, executionTask);
		}

		void cancel() {
			if (!canceled) {
				if (schedule != null) {
					schedule.cancel(false);
				}
				if (executionTask != null) {
					executionTask.cancel();
				}
				this.canceled = true;
				loader = null;
			}
		}

	}

}
