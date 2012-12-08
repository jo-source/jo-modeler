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
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Index;
import org.jowidgets.cap.service.jpa.api.query.FilterParameterConverter;
import org.jowidgets.cap.service.jpa.api.query.QueryPath;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.util.NullCompatibleEquivalence;

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

	@Basic
	@Column(name = "ORDINAL")
	private Integer order;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ICON_ID", nullable = true, insertable = false, updatable = false)
	private Icon icon;

	@Column(name = "ICON_ID", nullable = true)
	private Long iconId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_ICON_ID", nullable = true, insertable = false, updatable = false)
	private Icon createIcon;

	@Column(name = "CREATE_ICON_ID", nullable = true)
	private Long createIconId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELETE_ICON_ID", nullable = true, insertable = false, updatable = false)
	private Icon deleteIcon;

	@Column(name = "DELETE_ICON_ID", nullable = true)
	private Long deleteIconId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "CREATE_LINK_ICON_ID", nullable = true, insertable = false, updatable = false)
	private Icon createLinkIcon;

	@Column(name = "CREATE_LINK_ICON_ID", nullable = true)
	private Long createLinkIconId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "DELETE_LINK_ICON_ID", nullable = true, insertable = false, updatable = false)
	private Icon deleteLinkIcon;

	@Column(name = "DELETE_LINK_ICON_ID", nullable = true)
	private Long deleteLinkIconId;

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "parentModel")
	@BatchSize(size = 1000)
	private final Set<EntityPropertyModel> entityPropertyModels = new HashSet<EntityPropertyModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "destinationEntityModel")
	@BatchSize(size = 1000)
	private final Set<RelationModel> destinationEntityOfSourceEntityRelation = new HashSet<RelationModel>();

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "sourceEntityModel")
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

	@Override
	public Integer getOrder() {
		return order;
	}

	@Override
	public void setOrder(final Integer order) {
		this.order = order;
	}

	public void setIconId(final Long id) {
		this.iconId = id;
		if (!NullCompatibleEquivalence.equals(icon != null ? this.icon.getId() : null, iconId)) {
			if (iconId != null) {
				icon = EntityManagerProvider.get().find(Icon.class, iconId);
			}
			else {
				icon = null;
			}
		}
	}

	@Override
	@QueryPath(path = "iconId")
	@FilterParameterConverter(IconDescriptorQueryConverter.class)
	public IconDescriptor getIconDescriptor() {
		if (icon != null) {
			return icon.getDescriptor();
		}
		else {
			return null;
		}
	}

	@Override
	public void setIconDescriptor(final IconDescriptor iconDescriptor) {
		if (iconDescriptor != null) {
			setIconId(iconDescriptor.getIconId());
		}
		else {
			setIconId(null);
		}
	}

	public void setCreateIconId(final Long id) {
		this.createIconId = id;
		if (!NullCompatibleEquivalence.equals(createIcon != null ? this.createIcon.getId() : null, createIconId)) {
			if (createIconId != null) {
				createIcon = EntityManagerProvider.get().find(Icon.class, createIconId);
			}
			else {
				createIcon = null;
			}
		}
	}

	@Override
	@QueryPath(path = "createIconId")
	public IconDescriptor getCreateIconDescriptor() {
		if (createIcon != null) {
			return createIcon.getDescriptor();
		}
		else {
			return null;
		}
	}

	@Override
	public void setCreateIconDescriptor(final IconDescriptor iconDescriptor) {
		if (iconDescriptor != null) {
			setCreateIconId(iconDescriptor.getIconId());
		}
		else {
			setCreateIconId(null);
		}
	}

	public void setDeleteIconId(final Long id) {
		this.deleteIconId = id;
		if (!NullCompatibleEquivalence.equals(deleteIcon != null ? this.deleteIcon.getId() : null, deleteIconId)) {
			if (deleteIconId != null) {
				deleteIcon = EntityManagerProvider.get().find(Icon.class, deleteIconId);
			}
			else {
				deleteIcon = null;
			}
		}
	}

	@Override
	@QueryPath(path = "deleteIconId")
	public IconDescriptor getDeleteIconDescriptor() {
		if (deleteIcon != null) {
			return deleteIcon.getDescriptor();
		}
		else {
			return null;
		}
	}

	@Override
	public void setDeleteIconDescriptor(final IconDescriptor iconDescriptor) {
		if (iconDescriptor != null) {
			setDeleteIconId(iconDescriptor.getIconId());
		}
		else {
			setDeleteIconId(null);
		}
	}

	public void setCreateLinkIconId(final Long id) {
		this.createLinkIconId = id;
		if (!NullCompatibleEquivalence.equals(createLinkIcon != null ? this.createLinkIcon.getId() : null, createLinkIconId)) {
			if (createIconId != null) {
				createLinkIcon = EntityManagerProvider.get().find(Icon.class, createLinkIconId);
			}
			else {
				createLinkIcon = null;
			}
		}
	}

	@Override
	@QueryPath(path = "createLinkIconId")
	public IconDescriptor getCreateLinkIconDescriptor() {
		if (createLinkIcon != null) {
			return createLinkIcon.getDescriptor();
		}
		else {
			return null;
		}
	}

	@Override
	public void setCreateLinkIconDescriptor(final IconDescriptor iconDescriptor) {
		if (iconDescriptor != null) {
			setCreateLinkIconId(iconDescriptor.getIconId());
		}
		else {
			setCreateLinkIconId(null);
		}
	}

	public void setDeleteLinkIconId(final Long id) {
		this.deleteLinkIconId = id;
		if (!NullCompatibleEquivalence.equals(deleteLinkIcon != null ? this.deleteLinkIcon.getId() : null, deleteLinkIconId)) {
			if (deleteLinkIconId != null) {
				deleteLinkIcon = EntityManagerProvider.get().find(Icon.class, deleteLinkIconId);
			}
			else {
				deleteLinkIcon = null;
			}
		}
	}

	@Override
	@QueryPath(path = "deleteLinkIconId")
	public IconDescriptor getDeleteLinkIconDescriptor() {
		if (deleteLinkIcon != null) {
			return deleteLinkIcon.getDescriptor();
		}
		else {
			return null;
		}
	}

	@Override
	public void setDeleteLinkIconDescriptor(final IconDescriptor iconDescriptor) {
		if (iconDescriptor != null) {
			setDeleteLinkIconId(iconDescriptor.getIconId());
		}
		else {
			setDeleteLinkIconId(null);
		}
	}

	@Override
	@QueryPath(path = {"entityPropertyModels", "name"})
	public List<String> getPropertiesNames() {
		final List<String> result = new LinkedList<String>();
		for (final EntityPropertyModel property : entityPropertyModels) {
			result.add(property.getName());
		}
		return result;
	}

	@PreRemove
	private void preRemove() {
		final EntityManager em = EntityManagerProvider.get();
		for (final EntityPropertyModel property : entityPropertyModels) {
			em.remove(property);
		}
		final Set<RelationModel> removedRelations = new HashSet<RelationModel>();
		for (final RelationModel relationModel : sourceEntityOfDestinationEntityRelation) {
			if (!removedRelations.contains(relationModel)) {
				em.remove(relationModel);
				removedRelations.add(relationModel);
			}
		}
		for (final RelationModel relationModel : destinationEntityOfSourceEntityRelation) {
			if (!removedRelations.contains(relationModel)) {
				em.remove(relationModel);
				removedRelations.add(relationModel);
			}
		}
	}

}
