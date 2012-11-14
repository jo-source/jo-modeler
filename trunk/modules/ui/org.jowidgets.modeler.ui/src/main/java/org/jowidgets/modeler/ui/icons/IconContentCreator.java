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

import org.jowidgets.api.widgets.IComboBox;
import org.jowidgets.api.widgets.IComposite;
import org.jowidgets.api.widgets.IIcon;
import org.jowidgets.api.widgets.ITextControl;
import org.jowidgets.api.widgets.content.IInputContentContainer;
import org.jowidgets.api.widgets.content.IInputContentCreator;
import org.jowidgets.cap.ui.api.CapUiToolkit;
import org.jowidgets.cap.ui.api.widgets.ICapApiBluePrintFactory;
import org.jowidgets.common.types.Markup;
import org.jowidgets.common.widgets.layout.MigLayoutDescriptor;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.modeler.common.lookup.LookUpIds;
import org.jowidgets.tools.widgets.blueprint.BPF;

final class IconContentCreator implements IInputContentCreator<IconDescriptor> {

	@SuppressWarnings("unused")
	private IComboBox<Object> iconSetFilter;
	@SuppressWarnings("unused")
	private ITextControl iconFilter;

	@SuppressWarnings("unused")
	private IIcon currentIcon;
	@SuppressWarnings("unused")
	private ITextControl currentIconText;

	@Override
	public void createContent(final IInputContentContainer container) {
		final ICapApiBluePrintFactory cbpf = CapUiToolkit.bluePrintFactory();

		container.setLayout(new MigLayoutDescriptor("[grow, 0::]", "[][grow, 0::]"));
		final IComposite searchBar = container.add(BPF.composite(), "growx, w 0::");
		searchBar.setLayout(new MigLayoutDescriptor("[][grow, 0::]15[][grow, 0::]", "[][]15[][]"));

		searchBar.add(BPF.textSeparator("Filter").setMarkup(Markup.STRONG), "span 4, growx, w 0::, wrap");
		searchBar.add(BPF.textLabel("Icon set"));
		this.iconSetFilter = searchBar.add(cbpf.lookUpComboBox(LookUpIds.ICONS_SETS), "growx, sg g1");

		searchBar.add(BPF.textLabel("Icon"));
		this.iconFilter = searchBar.add(BPF.textField(), "growx, w 0::, sg g1, wrap");

		searchBar.add(BPF.textSeparator("Selected icon").setMarkup(Markup.STRONG), "span 4, growx, w 0::, wrap");
		currentIcon = searchBar.add(BPF.icon(), "w 16!");
		currentIconText = searchBar.add(BPF.textField().setEditable(false), "growx, w 0::, span 3");

	}

	@Override
	public void setValue(final IconDescriptor value) {
		// TODO Auto-generated method stub
	}

	@Override
	public IconDescriptor getValue() {
		// TODO Auto-generated method stub
		return null;
	}

}
