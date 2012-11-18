/*
 * Copyright (c) 2011, grossmann, Nikolaus Moll
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

import org.jowidgets.api.controller.IContainerListener;
import org.jowidgets.api.widgets.IContainer;
import org.jowidgets.api.widgets.IControl;
import org.jowidgets.common.types.Dimension;
import org.jowidgets.common.types.Rectangle;
import org.jowidgets.common.widgets.layout.ILayouter;
import org.jowidgets.tools.controller.ComponentAdapter;
import org.jowidgets.util.Assert;

final class IconCompositeLayout implements ILayouter {

	private static final int GAP = 6;

	private final IContainer container;

	private int maxX;
	private int maxY;
	private int columnCount;
	private int rowCount;
	private int lastLayoutedRowCount;
	private int lastLayoutedColumnCount;
	private boolean containerChanged;
	private boolean containerLayoutet;

	IconCompositeLayout(final IContainer container, final IContainer parent) {
		Assert.paramNotNull(container, "container");
		this.container = container;
		this.maxX = -1;
		this.maxY = -1;
		this.columnCount = -1;
		this.rowCount = -1;
		this.lastLayoutedRowCount = -1;
		this.lastLayoutedColumnCount = -1;
		this.containerChanged = true;
		this.containerLayoutet = false;
		container.addContainerListener(new IContainerListener() {

			@Override
			public void beforeRemove(final IControl control) {
				containerChanged = true;
				containerLayoutet = false;
			}

			@Override
			public void afterAdded(final IControl control) {
				containerChanged = true;
				containerLayoutet = false;
			}
		});

		parent.addComponentListener(new ComponentAdapter() {
			@Override
			public void sizeChanged() {
				layout();
			}
		});
	}

	@Override
	public void layout() {
		final Rectangle clientArea = container.getClientArea();

		int x = clientArea.getX();
		int y = clientArea.getY();

		checkCache();
		if (lastLayoutedRowCount != rowCount || lastLayoutedColumnCount != columnCount || !containerLayoutet) {
			container.setRedrawEnabled(false);
			int currentColumn = 0;
			for (final IControl control : container.getChildren()) {
				control.setSize(maxX, maxY);
				control.setPosition(x, y);
				if (currentColumn < columnCount) {
					x += (maxX + GAP);
					currentColumn++;
				}
				else {
					currentColumn = 0;
					x = 0;
					y += (maxY + GAP);
				}
			}
			containerLayoutet = true;
			lastLayoutedRowCount = rowCount;
			lastLayoutedColumnCount = columnCount;
			container.setRedrawEnabled(true);
		}
	}

	private void checkCache() {
		if (containerChanged) {
			maxX = 0;
			maxY = 0;
			for (final IControl control : container.getChildren()) {
				final Dimension size = control.getPreferredSize();
				maxX = Math.max(maxX, size.getWidth());
				maxY = Math.max(maxY, size.getWidth());
			}
			containerChanged = false;
		}
		columnCount = container.getSize().getWidth() / (maxX + GAP) - 2;
		rowCount = container.getChildren().size() / (columnCount + 1) + 1;
	}

	@Override
	public Dimension getMinSize() {
		return new Dimension(0, 0);
	}

	@Override
	public Dimension getPreferredSize() {
		checkCache();
		return new Dimension(container.getClientArea().getWidth(), rowCount * (maxY + GAP));
	}

	@Override
	public Dimension getMaxSize() {
		return new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE);
	}

	@Override
	public void invalidate() {}

}
