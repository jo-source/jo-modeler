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

package org.jowidgets.modeler.service.executor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jowidgets.cap.common.api.execution.IExecutionCallback;
import org.jowidgets.cap.service.api.executor.IBeanListExecutor;
import org.jowidgets.modeler.service.persistence.bean.AbstractPropertyModel;
import org.jowidgets.modeler.service.persistence.bean.Bean;

abstract class AbstractMovePropertiesExecutor implements IBeanListExecutor<AbstractPropertyModel, Void> {

	@Override
	public final List<AbstractPropertyModel> execute(
		final List<AbstractPropertyModel> properties,
		final Void parameter,
		final IExecutionCallback executionCallback) {

		final Map<Bean, ArrayList<AbstractPropertyModel>> propertiesByParent = new HashMap<Bean, ArrayList<AbstractPropertyModel>>();

		for (final AbstractPropertyModel property : properties) {
			final Bean parent = property.getParent();
			if (parent != null) {
				ArrayList<AbstractPropertyModel> propertiesForParent = propertiesByParent.get(parent);
				if (propertiesForParent == null) {
					propertiesForParent = new ArrayList<AbstractPropertyModel>();
					propertiesByParent.put(parent, propertiesForParent);
				}
				propertiesForParent.add(property);
			}
		}

		for (final Entry<Bean, ArrayList<AbstractPropertyModel>> entry : propertiesByParent.entrySet()) {
			moveForParentGroup(entry.getValue());
		}

		return properties;
	}

	protected abstract void moveForParentGroup(final ArrayList<AbstractPropertyModel> properties);

}