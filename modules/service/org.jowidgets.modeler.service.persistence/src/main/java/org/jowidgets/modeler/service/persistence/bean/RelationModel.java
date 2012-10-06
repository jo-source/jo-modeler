/*
 * Copyright (c) 2011, H.Westphal
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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.jowidgets.cap.common.api.bean.Cardinality;
import org.jowidgets.modeler.common.bean.IRelationModel;

@Entity
@Table(name = "RELATION_MODEL")
public class RelationModel extends Bean implements IRelationModel {

	@Basic
	private String name;

	@Basic
	private String label;

	@Basic
	private String inverseLabel;

	@Basic
	private Boolean symmetric;

	@Basic
	private Cardinality sourceCardinality;

	@Basic
	private Cardinality destinationCardinality;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "SOURCE_ENTITY_MODEL_ID", nullable = false, insertable = false, updatable = false)
	private EntityModel sourceEntityModel;

	@Column(name = "SOURCE_ENTITY_MODEL_ID", nullable = false)
	private Long sourceEntityModelId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DESTINATION_ENTITY_MODEL_ID", nullable = false, insertable = false, updatable = false)
	private EntityModel destinationEntityModel;

	@Column(name = "DESTINATION_ENTITY_MODEL_ID", nullable = false)
	private Long destinationEntityModelId;

	public EntityModel getSourceEntityModel() {
		return sourceEntityModel;
	}

	public EntityModel getDestinationEntityModel() {
		return destinationEntityModel;
	}

	@Override
	public Long getSourceEntityModelId() {
		return sourceEntityModelId;
	}

	@Override
	public void setSourceEntityModelId(final Long sourceEntityModelId) {
		this.sourceEntityModelId = sourceEntityModelId;
	}

	@Override
	public Long getDestinationEntityModelId() {
		return destinationEntityModelId;
	}

	@Override
	public void setDestinationEntityModelId(final Long destinationEntityModelId) {
		this.destinationEntityModelId = destinationEntityModelId;
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
	public String getInverseLabel() {
		return inverseLabel;
	}

	@Override
	public void setInverseLabel(final String inverseLabel) {
		this.inverseLabel = inverseLabel;
	}

	@Override
	public Boolean getSymmetric() {
		return symmetric;
	}

	@Override
	public void setSymmetric(final Boolean symmetric) {
		this.symmetric = symmetric;
	}

	@Override
	public Cardinality getSourceCardinality() {
		return sourceCardinality;
	}

	@Override
	public void setSourceCardinality(final Cardinality sourceCardinality) {
		this.sourceCardinality = sourceCardinality;
	}

	@Override
	public Cardinality getDestinationCardinality() {
		return destinationCardinality;
	}

	@Override
	public void setDestinationCardinality(final Cardinality destinationCardinality) {
		this.destinationCardinality = destinationCardinality;
	}

}
