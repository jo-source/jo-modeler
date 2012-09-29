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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.common.bean.IEntityPropertyModel;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ENTITY_MODEL_ID", "NAME"}))
public class EntityPropertyModel extends PropertyModel implements IEntityPropertyModel {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_MODEL_ID", nullable = false, insertable = false, updatable = false)
	private EntityModel entityModel;

	@Column(name = "ENTITY_MODEL_ID", nullable = false)
	private Long entityModelId;

	@Override
	public Long getEntityModelId() {
		return entityModelId;
	}

	@Override
	public void setEntityModelId(final Long id) {
		this.entityModelId = id;
	}

	@PrePersist
	private void generateOrder() {
		if (getOrder() == null) {
			if (entityModel != null) {
				setOrder(getNextOrdinal(entityModel));
			}
			else if (entityModelId != null) {
				setOrder(getNextOrdinal(EntityManagerProvider.get().find(EntityModel.class, entityModelId)));
			}
		}
	}

	private static Integer getNextOrdinal(final EntityModel entityModel) {
		if (entityModel != null) {
			return entityModel.getEntityPropertyModels().size();
		}
		else {
			return null;
		}
	}

}