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
package org.jowidgets.modeler.common.bean;

import java.util.LinkedList;
import java.util.List;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.security.common.api.annotation.CreateAuthorization;
import org.jowidgets.cap.security.common.api.annotation.DeleteAuthorization;
import org.jowidgets.cap.security.common.api.annotation.ReadAuthorization;
import org.jowidgets.cap.security.common.api.annotation.UpdateAuthorization;
import org.jowidgets.modeler.common.security.ModelerAuthKeys;

@CreateAuthorization(ModelerAuthKeys.CREATE_ICON)
@ReadAuthorization(ModelerAuthKeys.READ_ICON)
@UpdateAuthorization(ModelerAuthKeys.UPDATE_ICON)
@DeleteAuthorization(ModelerAuthKeys.DELETE_ICON)
public interface IIcon extends IBean {

	String ICON_SET_ID_PROPERTY = "iconSetId";
	String KEY_PROPERTY = "key";
	String LABEL_PROPERTY = "label";
	String BYTES_PROPERTY = "bytes";
	String SIZE_PROPERTY = "size";

	List<String> ALL_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(ICON_SET_ID_PROPERTY);
			add(KEY_PROPERTY);
			add(LABEL_PROPERTY);
			add(BYTES_PROPERTY);
			add(SIZE_PROPERTY);
		}
	};

	@NotNull
	@Size(min = 1, max = 255)
	String getKey();

	void setKey(String key);

	@Size(max = 25)
	String getLabel();

	void setLabel(String label);

	Long getIconSetId();

	void setIconSetId(Long id);

	byte[] getBytes();

	int getSize();

}
