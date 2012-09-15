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

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Index;
import org.jowidgets.modeler.common.bean.IPropertyModel;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class PropertyModel extends Bean implements IPropertyModel {

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

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "propertyModel")
	@BatchSize(size = 1000)
	private final Set<EntityModelPropertyModelLink> entityModelPropertyModelLinks = new HashSet<EntityModelPropertyModelLink>();

	public Set<EntityModelPropertyModelLink> getEntityModelPropertyModelLinks() {
		return entityModelPropertyModelLinks;
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

}
