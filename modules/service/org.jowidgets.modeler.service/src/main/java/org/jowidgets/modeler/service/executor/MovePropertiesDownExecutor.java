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

package org.jowidgets.modeler.service.executor;

import java.util.ArrayList;
import java.util.Collections;

import javax.persistence.EntityManager;

import org.jowidgets.cap.common.api.exception.ExecutableCheckException;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.service.persistence.bean.AbstractPropertyModel;

public final class MovePropertiesDownExecutor extends AbstractMovePropertiesExecutor {

	private static final IMessage AT_LAST_POSITION = Messages.getMessage("MovePropertiesDownExecutor.alreadyAtLastPosition");

	@Override
	protected void moveForParentGroup(final ArrayList<AbstractPropertyModel> properties) {
		Collections.sort(properties);
		final AbstractPropertyModel lastProperty = properties.get(properties.size() - 1);
		final ArrayList<AbstractPropertyModel> allProperties = lastProperty.getAllPropertiesOfParent();

		if (lastProperty.getOrder().intValue() >= allProperties.get(allProperties.size() - 1).getOrder().intValue()) {
			throw new ExecutableCheckException(lastProperty.getId(), "Already at last position", AT_LAST_POSITION.get());
		}

		final EntityManager em = EntityManagerProvider.get();

		for (int i = properties.size() - 1; i >= 0; i--) {
			final AbstractPropertyModel property = properties.get(i);
			final int order = property.getOrder().intValue();
			property.setOrder(Integer.valueOf(order + 1));
			em.persist(property);

			final AbstractPropertyModel swapProperty = allProperties.get(order + 1);
			swapProperty.setOrder(Integer.valueOf(order));
			em.persist(swapProperty);

			Collections.sort(allProperties);
		}
	}

}
