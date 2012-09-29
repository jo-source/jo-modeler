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

@CreateAuthorization(ModelerAuthKeys.CREATE_ENTITY_MODEL)
@ReadAuthorization(ModelerAuthKeys.READ_ENTITY_MODEL)
@UpdateAuthorization(ModelerAuthKeys.UPDATE_ENTITY_MODEL)
@DeleteAuthorization(ModelerAuthKeys.DELETE_ENTITY_MODEL)
public interface IEntityModel extends IBean {

	String NAME_PROPERTY = "name";
	String LABEL_SINGULAR_PROPERTY = "labelSingular";
	String LABEL_PLURAL_PROPERTY = "labelPlural";
	String RENDERING_PATTERN_PROPERTY = "renderingPattern";
	String PROPERTIES_NAMES_PROPERTY = "propertiesNames";

	List<String> ALL_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(NAME_PROPERTY);
			add(LABEL_SINGULAR_PROPERTY);
			add(LABEL_PLURAL_PROPERTY);
			add(RENDERING_PATTERN_PROPERTY);
			add(PROPERTIES_NAMES_PROPERTY);
		}
	};

	@NotNull
	@Size(min = 2, max = 25)
	String getName();

	void setName(String name);

	@NotNull
	@Size(min = 2, max = 25)
	String getLabelSingular();

	void setLabelSingular(String label);

	@NotNull
	@Size(min = 2, max = 25)
	String getLabelPlural();

	void setLabelPlural(String label);

	@Size(min = 2, max = 100)
	String getRenderingPattern();

	void setRenderingPattern(String pattern);

	List<String> getPropertiesNames();
}
