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

import org.jowidgets.api.image.IconsSmall;
import org.jowidgets.api.toolkit.Toolkit;
import org.jowidgets.api.widgets.IButton;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.IInputDialog;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.blueprint.IInputDialogBluePrint;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.widgets.controller.IActionListener;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.tools.widgets.blueprint.BPF;
import org.jowidgets.tools.widgets.wrapper.AbstractInputControl;
import org.jowidgets.util.NullCompatibleEquivalence;
import org.jowidgets.validation.IValidationResult;
import org.jowidgets.validation.ValidationResult;

public final class IconSelectionControl extends AbstractInputControl<IconDescriptor> {

	private final IIcon icon;
	private final ITextControl textField;
	private final IButton editButton;

	private IconDescriptor value;
	private IconDescriptor lastUnmodifiedValue;

	public IconSelectionControl(final IComposite composite) {
		super(composite);
		composite.setLayout(new MigLayoutDescriptor("hidemode 2", "0[]0[grow, 0::][]0", "0[]0"));

		this.icon = composite.add(BPF.icon());
		icon.setVisible(false);

		this.textField = composite.add(BPF.textField().setEditable(false), "growx, w 0::");

		final int width = composite.getPreferredSize().getHeight() + 2;
		this.editButton = composite.add(BPF.button().setIcon(IconsSmall.EDIT), "grow, h ::" + width + ", w ::" + width);
		editButton.addActionListener(new IActionListener() {
			@Override
			public void actionPerformed() {
				openEditDialog();
			}
		});
	}

	private void openEditDialog() {
		final IInputDialogBluePrint<IconDescriptor> inputDialogBp = BPF.inputDialog(new IconContentCreator());
		inputDialogBp.setValidationLabel(null);
		inputDialogBp.setMinPackSize(new Dimension(800, 600));
		//TODO MG i18n
		inputDialogBp.setIcon(IconsSmall.EDIT).setTitle("Edit icon");
		final IInputDialog<IconDescriptor> inputDialog = Toolkit.getActiveWindow().createChildWindow(inputDialogBp);
		inputDialog.setValue(getValue());
		inputDialog.setVisible(true);
		if (inputDialog.isOkPressed()) {
			setValue(inputDialog.getValue());
		}
	}

	@Override
	public boolean hasModifications() {
		return !NullCompatibleEquivalence.equals(lastUnmodifiedValue, value);
	}

	@Override
	public void resetModificationState() {
		lastUnmodifiedValue = value;
	}

	@Override
	public void setValue(final IconDescriptor value) {
		this.value = value;
		if (value != null) {
			icon.setVisible(true);
			icon.setIcon(new DynamicIcon(value));
			textField.setText(value.getIconLabel() + " (" + value.getIconSetLabel() + ")");
		}
		else {
			icon.setVisible(false);
			icon.setIcon(null);
			textField.setText(null);
		}
	}

	@Override
	public IconDescriptor getValue() {
		return value;
	}

	@Override
	public void setEditable(final boolean editable) {}

	@Override
	protected IValidationResult createValidationResult() {
		return ValidationResult.ok();
	}

}
