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

import java.util.ArrayList;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.common.bean.IEntityPropertyModel;
import org.jowidgets.util.NullCompatibleEquivalence;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"ENTITY_MODEL_ID", "NAME"}))
public class EntityPropertyModel extends AbstractPropertyModel implements IEntityPropertyModel {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ENTITY_MODEL_ID", nullable = false, insertable = false, updatable = false)
	private EntityModel parentModel;

	@Column(name = "ENTITY_MODEL_ID", nullable = false)
	private Long parentModelId;

	@Override
	public Long getParentModelId() {
		return parentModelId;
	}

	@Override
	public void setParentModelId(final Long id) {
		this.parentModelId = id;
		if (this.parentModel != null && !NullCompatibleEquivalence.equals(this.parentModel.getId(), parentModelId)) {
			parentModel = null;
		}
	}

	@Override
	public Bean getParent() {
		return getEntityModel();
	}

	@Override
	public ArrayList<AbstractPropertyModel> getAllPropertiesOfParent() {
		final Bean parent = getParent();
		final ArrayList<AbstractPropertyModel> result = new ArrayList<AbstractPropertyModel>();
		if (parent != null) {
			final EntityManager em = EntityManagerProvider.get();
			final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
			final CriteriaQuery<EntityPropertyModel> query = criteriaBuilder.createQuery(EntityPropertyModel.class);
			final Root<EntityPropertyModel> root = query.from(EntityPropertyModel.class);
			query.where(criteriaBuilder.equal(root.get(IEntityPropertyModel.PARENT_MODEL_ID_PROPERTY), parent.getId()));
			query.orderBy(criteriaBuilder.asc(root.get(IEntityPropertyModel.ORDER_PROPERTY)));
			result.addAll(em.createQuery(query).getResultList());
		}
		return result;
	}

	public EntityModel getEntityModel() {
		if (parentModel != null) {
			return parentModel;
		}
		else if (parentModelId != null) {
			parentModel = EntityManagerProvider.get().find(EntityModel.class, parentModelId);
		}
		return parentModel;
	}

}
