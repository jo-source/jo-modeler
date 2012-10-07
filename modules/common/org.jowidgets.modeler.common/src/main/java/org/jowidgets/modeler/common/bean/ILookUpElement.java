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

@CreateAuthorization(ModelerAuthKeys.CREATE_LOOK_UP)
@ReadAuthorization(ModelerAuthKeys.READ_LOOK_UP)
@UpdateAuthorization(ModelerAuthKeys.UPDATE_LOOK_UP)
@DeleteAuthorization(ModelerAuthKeys.DELETE_LOOK_UP)
public interface ILookUpElement extends IBean {

	String LOOK_UP_ID_PROPERTY = "lookUpId";
	String KEY_PROPERTY = "key";
	String LABEL_PROPERTY = "label";
	String LABEL_LONG_PROPERTY = "labelLong";
	String DESCRIPTION_PROPERTY = "description";
	String IS_VALID_PROPERTY = "isValid";

	List<String> ALL_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(LOOK_UP_ID_PROPERTY);
			add(KEY_PROPERTY);
			add(LABEL_PROPERTY);
			add(LABEL_LONG_PROPERTY);
			add(DESCRIPTION_PROPERTY);
			add(IS_VALID_PROPERTY);
		}
	};

	@NotNull
	@Size(min = 2, max = 25)
	String getKey();

	void setKey(String key);

	@Size(max = 25)
	String getLabel();

	void setLabel(String label);

	@Size(max = 25)
	String getLabelLong();

	void setLabelLong(String labelLong);

	@Size(max = 200)
	String getDescription();

	void setDescription(String description);

	@NotNull
	Boolean getIsValid();

	void setIsValid(Boolean isValid);

	Long getLookUpId();

	void setLookUpId(Long id);

}
