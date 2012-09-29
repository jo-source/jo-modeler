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
import java.util.Collections;

import javax.persistence.EntityManager;

import org.jowidgets.cap.common.api.exception.ExecutableCheckException;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.service.persistence.bean.AbstractPropertyModel;

public final class MovePropertiesUpExecutor extends AbstractMovePropertiesExecutor {

	@Override
	protected void moveForParentGroup(final ArrayList<AbstractPropertyModel> properties) {
		Collections.sort(properties);
		final AbstractPropertyModel firstProperty = properties.iterator().next();
		final ArrayList<AbstractPropertyModel> allProperties = firstProperty.getAllPropertiesOfParent();

		if (firstProperty.getOrder().intValue() <= allProperties.get(0).getOrder().intValue()) {
			//TODO MG i18n
			final String userMessage = "Is already at first position";
			throw new ExecutableCheckException(firstProperty.getId(), "Is already at first position", userMessage);
		}

		final EntityManager em = EntityManagerProvider.get();

		for (int i = 0; i < properties.size(); i++) {
			final AbstractPropertyModel property = properties.get(i);
			final int order = property.getOrder().intValue();
			property.setOrder(Integer.valueOf(order - 1));
			em.persist(property);

			final AbstractPropertyModel swapProperty = allProperties.get(order - 1);
			swapProperty.setOrder(Integer.valueOf(order));
			em.persist(swapProperty);

			Collections.sort(allProperties);
		}
	}

}
