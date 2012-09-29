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
import org.jowidgets.modeler.common.bean.IEntityModel;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
public class EntityModel extends Bean implements IEntityModel {

	@Basic
	@Index(name = "NameIndex")
	private String name;

	@Basic
	private String labelSingular;

	@Basic
	private String labelPlural;

	@Basic
	private String renderingPattern;

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "entityModel")
	@BatchSize(size = 1000)
	private final Set<EntityPropertyModel> entityPropertyModels = new HashSet<EntityPropertyModel>();

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "destinationEntityModel")
	@BatchSize(size = 1000)
	private final Set<RelationModel> destinationEntityOfSourceEntityRelation = new HashSet<RelationModel>();

	@OneToMany(cascade = CascadeType.REMOVE, fetch = FetchType.LAZY, mappedBy = "sourceEntityModel")
	@BatchSize(size = 1000)
	private final Set<RelationModel> sourceEntityOfDestinationEntityRelation = new HashSet<RelationModel>();;

	public Set<EntityPropertyModel> getEntityPropertyModels() {
		return entityPropertyModels;
	}

	public Set<RelationModel> getSourceEntityOfDestinationEntityRelation() {
		return sourceEntityOfDestinationEntityRelation;
	}

	public Set<RelationModel> getDestinationEntityOfSourceEntityRelation() {
		return destinationEntityOfSourceEntityRelation;
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
	public String getLabelSingular() {
		return labelSingular;
	}

	@Override
	public void setLabelSingular(final String labelSingular) {
		this.labelSingular = labelSingular;
	}

	@Override
	public String getLabelPlural() {
		return labelPlural;
	}

	@Override
	public void setLabelPlural(final String labelPlural) {
		this.labelPlural = labelPlural;
	}

	@Override
	public String getRenderingPattern() {
		return renderingPattern;
	}

	@Override
	public void setRenderingPattern(final String renderingPattern) {
		this.renderingPattern = renderingPattern;
	}

}
