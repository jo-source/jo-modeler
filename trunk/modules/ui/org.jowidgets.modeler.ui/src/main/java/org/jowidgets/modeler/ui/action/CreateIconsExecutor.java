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

package org.jowidgets.modeler.ui.action;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.jowidgets.api.command.IExecutionContext;
import org.jowidgets.api.threads.IUiThreadAccess;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IFileChooser;
import org.jowidgets.api.widgets.blueprint.IFileChooserBluePrint;
import org.jowidgets.cap.common.api.execution.IExecutionCallbackListener;
import org.jowidgets.cap.common.api.execution.IResultCallback;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.bean.IBeanProxy;
import org.jowidgets.cap.ui.api.execution.IExecutionTask;
import org.jowidgets.cap.ui.api.table.IBeanTableModel;
import org.jowidgets.cap.ui.api.widgets.ICapApiBluePrintFactory;
import org.jowidgets.cap.ui.api.widgets.IExecutionTaskDialog;
import org.jowidgets.cap.ui.api.widgets.IExecutionTaskDialogBluePrint;
import org.jowidgets.cap.ui.tools.execution.AbstractSingleBeanExecutor;
import org.jowidgets.cap.ui.tools.execution.AbstractUiResultCallback;
import org.jowidgets.common.types.DialogResult;
import org.jowidgets.common.types.FileChooserType;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.i18n.api.MessageReplacer;
import org.jowidgets.modeler.common.bean.IIconSet;
import org.jowidgets.modeler.common.service.IIconCreatorService;
import org.jowidgets.service.api.ServiceProvider;
import org.jowidgets.tools.types.FileChooserFilter;
import org.jowidgets.tools.widgets.blueprint.BPF;

final class CreateIconsExecutor extends AbstractSingleBeanExecutor<IIconSet, Void> {

	private static final IMessage CAN_NOT_OPEN = Messages.getMessage("CreateIconsExecutor.canNotOpen");
	private static final IMessage ALL_ICONS_ADDED = Messages.getMessage("CreateIconsExecutor.allIconsAdded");
	private static final IMessage ERROR_ADDING_ICONS = Messages.getMessage("CreateIconsExecutor.errorAddingIcons");

	private static final List<FileChooserFilter> FILTER_LIST = Collections.singletonList(new FileChooserFilter(
		"png, gif",
		"png",
		"gif"));

	private final IBeanTableModel<IIconSet> model;

	private File lastSelectedFile;

	CreateIconsExecutor(final IBeanTableModel<IIconSet> model) {
		super();
		this.model = model;
	}

	@Override
	protected void execute(final IExecutionContext executionContext, final IBeanProxy<IIconSet> bean, final Void defaultParameter) throws Exception {
		//this executor will create an own execution task
		bean.setExecutionTask(null);

		final IFileChooserBluePrint fileChooserBp = BPF.fileChooser(FileChooserType.OPEN_FILE_LIST);
		fileChooserBp.setTitle(executionContext.getAction().getText());
		fileChooserBp.setFilterList(FILTER_LIST);

		final IFileChooser fileChooser = Toolkit.getActiveWindow().createChildWindow(fileChooserBp);
		if (lastSelectedFile != null) {
			fileChooser.setSelectedFile(lastSelectedFile);
		}

		final DialogResult dialogResult = fileChooser.open();
		if (dialogResult == DialogResult.OK) {
			final List<File> selectedFiles = fileChooser.getSelectedFiles();
			if (selectedFiles.size() > 0) {
				lastSelectedFile = selectedFiles.iterator().next();
				createIcons(executionContext, bean, selectedFiles);
			}
		}
	}

	private void createIcons(
		final IExecutionContext executionContext,
		final IBeanProxy<IIconSet> bean,
		final List<File> selectedFiles) {

		final IExecutionTask executionTask = CapUiToolkit.executionTaskFactory().create();
		bean.setExecutionTask(executionTask);

		final int filesCount = selectedFiles.size();

		final String[] keys = new String[filesCount];
		final InputStream[] inputStreams = new InputStream[filesCount];

		final Iterator<File> filesIterator = selectedFiles.iterator();
		for (int i = 0; i < filesCount; i++) {
			final File file = filesIterator.next();
			keys[i] = file.getName().split("\\.")[0];
			try {
				inputStreams[i] = new FileInputStream(file);
			}
			catch (final FileNotFoundException e) {
				final String message = MessageReplacer.replace(CAN_NOT_OPEN.get(), file.getName());
				Toolkit.getMessagePane().showError(executionContext, message);
				return;
			}
		}

		final IUiThreadAccess uiThreadAccess = Toolkit.getUiThreadAccess();
		executionTask.addExecutionCallbackListener(new IExecutionCallbackListener() {
			@Override
			public void canceled() {
				uiThreadAccess.invokeLater(new Runnable() {
					@Override
					public void run() {
						bean.setExecutionTask(null);
					}
				});

			}
		});
		final ICapApiBluePrintFactory cbpf = CapUiToolkit.bluePrintFactory();
		final IExecutionTaskDialogBluePrint executionTaskDialogBp = cbpf.executionTaskDialog(executionTask).setModal(false);
		executionTaskDialogBp.setExecutionContext(executionContext);
		final IExecutionTaskDialog executionTaskDialog = Toolkit.getActiveWindow().createChildWindow(executionTaskDialogBp);

		final IResultCallback<Void> resultCallback = new AbstractUiResultCallback<Void>() {
			@Override
			protected void finishedUi(final Void result) {
				clearExecutionTask();
				final List<IBeanProxy<IIconSet>> selectedBeans = model.getSelectedBeans();
				if (selectedBeans.contains(bean)) {
					final Collection<IBeanProxy<IIconSet>> emptyList = Collections.emptyList();
					model.setSelectedBeans(emptyList);
					model.setSelectedBeans(selectedBeans);
				}
				executionTaskDialog.executionFinished(ALL_ICONS_ADDED.get());
			}

			@Override
			protected void exceptionUi(final Throwable exception) {
				clearExecutionTask();
				executionTaskDialog.executionError(ERROR_ADDING_ICONS.get());
			}

			private void clearExecutionTask() {
				bean.setExecutionTask(null);
			}
		};

		ServiceProvider.getService(IIconCreatorService.ID).create(resultCallback, bean.getId(), keys, inputStreams, executionTask);
		executionTaskDialog.setVisible(true);
	}
}
