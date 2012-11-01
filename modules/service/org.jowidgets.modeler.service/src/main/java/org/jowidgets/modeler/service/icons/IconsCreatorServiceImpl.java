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

package org.jowidgets.modeler.service.icons;

import java.io.InputStream;

import javax.persistence.EntityManager;

import org.jowidgets.cap.common.api.exception.DeletedBeanException;
import org.jowidgets.cap.common.api.execution.IExecutionCallback;
import org.jowidgets.cap.common.api.execution.IResultCallback;
import org.jowidgets.cap.service.api.CapServiceToolkit;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.i18n.api.MessageReplacer;
import org.jowidgets.modeler.common.service.IIconCreatorService;
import org.jowidgets.modeler.service.persistence.bean.Icon;
import org.jowidgets.modeler.service.persistence.bean.IconSet;
import org.jowidgets.util.Assert;
import org.jowidgets.util.io.IoUtils;

public class IconsCreatorServiceImpl implements IIconCreatorService {

	private static final IMessage ADD = Messages.getMessage("IconsCreatorServiceImpl.add");

	@Override
	public void create(
		final IResultCallback<Void> result,
		final Object iconsSetId,
		final String[] keys,
		final InputStream[] icons,
		final IExecutionCallback executionCallback) {
		try {
			createSync(iconsSetId, keys, icons, executionCallback);
			result.finished(null);
		}
		catch (final Exception exception) {
			result.exception(exception);
		}

	}

	private void createSync(
		final Object iconsSetId,
		final String[] keys,
		final InputStream[] icons,
		final IExecutionCallback executionCallback) {

		Assert.paramNotNull(iconsSetId, "iconsSetId");
		Assert.paramNotNull(keys, "keys");
		Assert.paramNotNull(icons, "icons");
		Assert.paramNotNull(executionCallback, "executionCallback");

		final EntityManager em = EntityManagerProvider.get();
		final IconSet iconSet = em.find(IconSet.class, iconsSetId);
		if (iconSet != null) {
			addIcons(iconSet, keys, icons, em, executionCallback);
		}
		else {
			throw new DeletedBeanException(iconsSetId);
		}
	}

	private void addIcons(
		final IconSet iconSet,
		final String[] keys,
		final InputStream[] icons,
		final EntityManager em,
		final IExecutionCallback executionCallback) {

		executionCallback.setTotalStepCount(keys.length);

		final String addMessage = ADD.get();

		for (int i = 0; i < keys.length; i++) {
			CapServiceToolkit.checkCanceled(executionCallback);

			final String key = keys[i];
			Assert.paramNotEmpty(key, "keys[" + i + "]");

			executionCallback.setDescription(MessageReplacer.replace(addMessage, key));

			final InputStream inputStream = icons[i];
			Assert.paramNotNull(inputStream, "icons[" + i + "]");

			addIcon(iconSet, key, inputStream, em);

			executionCallback.workedOne();
		}
	}

	private void addIcon(final IconSet iconSet, final String key, final InputStream inputStream, final EntityManager em) {
		final Icon icon = new Icon();
		icon.setIconSetId(iconSet.getId());
		icon.setKey(key);
		icon.setBytes(getBytes(inputStream));
		em.persist(icon);
	}

	private byte[] getBytes(final InputStream inputStream) {
		final byte[] result = IoUtils.toByteArray(inputStream);
		IoUtils.tryCloseSilent(inputStream);
		return result;
	}
}
