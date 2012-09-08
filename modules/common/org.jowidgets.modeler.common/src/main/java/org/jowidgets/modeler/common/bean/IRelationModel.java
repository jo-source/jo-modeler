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

import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.security.common.api.annotation.CreateAuthorization;
import org.jowidgets.cap.security.common.api.annotation.DeleteAuthorization;
import org.jowidgets.cap.security.common.api.annotation.ReadAuthorization;
import org.jowidgets.cap.security.common.api.annotation.UpdateAuthorization;
import org.jowidgets.modeler.common.security.ModelerAuthKeys;

@CreateAuthorization(ModelerAuthKeys.CREATE_RELATION_MODEL)
@ReadAuthorization(ModelerAuthKeys.READ_RELATION_MODEL)
@UpdateAuthorization(ModelerAuthKeys.UPDATE_RELATION_MODEL)
@DeleteAuthorization(ModelerAuthKeys.DELETE_RELATION_MODEL)
public interface IRelationModel extends IBean {

	String SOURCE_ENTITY_MODEL_ID_PROPERTY = "sourceEntityModelId";
	String DESTINATION_ENTITY_MODEL_ID_PROPERTY = "destinationEntityModelId";
	String NAME_PROPERTY = "name";
	String LABEL_PROPERTY = "label";
	String INVERSE_PROPERTY = "inverseLabel";
	String SYMMETRIC_PROPERTY = "symmetric";
	String SOURCE_CARDINALITY_PROPERTY = "sourceCardinality";
	String DESTINATION_CARDINALITY_PROPERTY = "destinationCardinality";

	List<String> ALL_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(IBean.ID_PROPERTY);
			add(SOURCE_ENTITY_MODEL_ID_PROPERTY);
			add(DESTINATION_ENTITY_MODEL_ID_PROPERTY);
			add(NAME_PROPERTY);
			add(LABEL_PROPERTY);
			add(INVERSE_PROPERTY);
			add(SYMMETRIC_PROPERTY);
			add(SOURCE_CARDINALITY_PROPERTY);
			add(DESTINATION_CARDINALITY_PROPERTY);
			add(IBean.VERSION_PROPERTY);
		}
	};

	@NotNull
	Long getSourceEntityModelId();

	void setSourceEntityModelId(Long id);

	@NotNull
	Long getDestinationEntityModelId();

	void setDestinationEntityModelId(Long id);

	@NotNull
	@Size(min = 2, max = 25)
	String getName();

	void setName(String name);

	@NotNull
	@Size(min = 1, max = 25)
	String getLabel();

	void setLabel(String label);

	@Size(min = 1, max = 25)
	String getInverseLabel();

	void setInverseLabel(String inverseLabel);

	@NotNull
	Boolean getSymmetric();

	void setSymmetric(Boolean visible);

	@NotNull
	Cardinality getSourceCardinality();

	void setSourceCardinality(Cardinality cardinality);

	@NotNull
	Cardinality getDestinationCardinality();

	void setDestinationCardinality(Cardinality cardinality);
}
