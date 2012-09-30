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

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.jowidgets.cap.common.api.bean.IBean;

public interface IPropertyModel extends IBean {

	String ORDER_PROPERTY = "order";
	String NAME_PROPERTY = "name";
	String LABEL_PROPERTY = "label";
	String LABEL_LONG_PROPERTY = "labelLong";
	String DESCRIPTION_PROPERTY = "description";
	String VISIBLE_PROPERTY = "visible";
	String MANDATORY_PROPERTY = "mandatory";
	String EDITABLE_PROPERTY = "editable";
	String SEARCHABLE_PROPERTY = "searchable";
	String VALUE_TYPE_PROPERTY = "valueType";
	String TABLE_WIDTH_PROPERTY = "tableWidth";

	List<String> ALL_COMMON_PROPERTIES = new LinkedList<String>() {
		private static final long serialVersionUID = 1L;
		{
			add(ORDER_PROPERTY);
			add(NAME_PROPERTY);
			add(LABEL_PROPERTY);
			add(LABEL_LONG_PROPERTY);
			add(DESCRIPTION_PROPERTY);
			add(VISIBLE_PROPERTY);
			add(MANDATORY_PROPERTY);
			add(EDITABLE_PROPERTY);
			add(SEARCHABLE_PROPERTY);
			add(VALUE_TYPE_PROPERTY);
			add(TABLE_WIDTH_PROPERTY);
		}
	};

	Integer getOrder();

	@NotNull
	@Size(min = 2, max = 25)
	String getName();

	void setName(String name);

	@NotNull
	@Size(min = 1, max = 25)
	String getLabel();

	void setLabel(String label);

	@Size(max = 40)
	String getLabelLong();

	void setLabelLong(String labelLong);

	@Size(max = 100)
	String getDescription();

	void setDescription(String description);

	@NotNull
	Boolean getVisible();

	void setVisible(Boolean visible);

	@NotNull
	Boolean getMandatory();

	void setMandatory(Boolean mandatory);

	@NotNull
	Boolean getEditable();

	void setEditable(Boolean editable);

	@NotNull
	Boolean getSearchable();

	void setSearchable(Boolean searchable);

	@NotNull
	@Size(min = 1, max = 500)
	String getValueType();

	void setValueType(String type);

	@Min(0)
	@Max(500)
	Integer getTableWidth();

	void setTableWidth(Integer width);
}
