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
package org.jowidgets.modeler.service.persistence.bean;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.Index;
import org.jowidgets.modeler.common.bean.IPropertyModel;

@MappedSuperclass
public class PropertyModel extends Bean implements IPropertyModel, Comparable<PropertyModel> {

	@Basic
	@Column(name = "ORDINAL")
	private Integer order;

	@Basic
	@Index(name = "NameIndex")
	private String name;

	@Basic
	private String label;

	@Basic
	private String labelLong;

	@Basic
	private String description;

	@Basic
	private Boolean visible;

	@Basic
	private Boolean mandatory;

	@Basic
	private Boolean editable;

	@Basic
	private Boolean searchable;

	@Basic
	private String valueType;

	@Basic
	private Integer tableWidth;

	@Override
	public Integer getOrder() {
		return order;
	}

	public void setOrder(final Integer order) {
		this.order = order;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(final String name) {
		this.name = name;
	}

	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void setLabel(final String label) {
		this.label = label;
	}

	@Override
	public String getLabelLong() {
		return labelLong;
	}

	@Override
	public void setLabelLong(final String labelLong) {
		this.labelLong = labelLong;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public void setDescription(final String description) {
		this.description = description;
	}

	@Override
	public Boolean getVisible() {
		return visible;
	}

	@Override
	public void setVisible(final Boolean visible) {
		this.visible = visible;
	}

	@Override
	public Boolean getMandatory() {
		return mandatory;
	}

	@Override
	public void setMandatory(final Boolean mandatory) {
		this.mandatory = mandatory;
	}

	@Override
	public Boolean getEditable() {
		return editable;
	}

	@Override
	public void setEditable(final Boolean editable) {
		this.editable = editable;
	}

	@Override
	public Boolean getSearchable() {
		return searchable;
	}

	@Override
	public void setSearchable(final Boolean searchable) {
		this.searchable = searchable;
	}

	@Override
	public String getValueType() {
		return valueType;
	}

	@Override
	public void setValueType(final String valueType) {
		this.valueType = valueType;
	}

	@Override
	public Integer getTableWidth() {
		return tableWidth;
	}

	@Override
	public void setTableWidth(final Integer width) {
		this.tableWidth = width;
	}

	@Override
	public int compareTo(final PropertyModel propertyModel) {
		if (order != null && propertyModel.order != null) {
			return order.intValue() - propertyModel.order.intValue();
		}
		else if (order == null && propertyModel.order == null) {
			return 0;
		}
		else if (order == null) {
			return -1;
		}
		else {// propertyModel.order == null
			return 1;
		}
	}

}
