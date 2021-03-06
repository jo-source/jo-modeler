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
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.hibernate.annotations.Index;
import org.jowidgets.cap.service.jpa.api.query.QueryPath;
import org.jowidgets.cap.service.jpa.tools.entity.EntityManagerProvider;
import org.jowidgets.modeler.common.bean.IIcon;
import org.jowidgets.modeler.common.dto.IconDescriptor;
import org.jowidgets.util.EmptyCheck;
import org.jowidgets.util.NullCompatibleEquivalence;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"key", "ICON_SET_ID"}))
public class Icon extends Bean implements IIcon {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ICON_SET_ID", nullable = false, insertable = false, updatable = false)
	private IconSet iconSet;

	@Column(name = "ICON_SET_ID", nullable = false)
	private Long iconSetId;

	@Basic
	@Index(name = "KeyIndex")
	private String key;

	@Basic
	private String label;

	@Lob
	private byte[] bytes;

	@Override
	public Long getIconSetId() {
		return iconSetId;
	}

	@Override
	public void setIconSetId(final Long id) {
		this.iconSetId = id;
		if (this.iconSet != null && !NullCompatibleEquivalence.equals(this.iconSet.getId(), iconSetId)) {
			iconSet = EntityManagerProvider.get().find(IconSet.class, iconSetId);
		}
	}

	@Override
	public String getKey() {
		return key;
	}

	@Override
	public void setKey(final String key) {
		this.key = key;
	}

	@Override
	public String getLabel() {
		if (!EmptyCheck.isEmpty(label)) {
			return label;
		}
		else {
			return getKey();
		}
	}

	@Override
	public void setLabel(final String label) {
		this.label = label;
	}

	@Override
	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(final byte[] bytes) {
		this.bytes = bytes;
	}

	@Override
	public int getSize() {
		if (bytes != null) {
			return bytes.length;
		}
		return 0;
	}

	@Override
	@QueryPath(path = {"iconSetId"})
	public String getIconSetLabel() {
		if (iconSet != null) {
			return iconSet.getLabel();
		}
		else {
			return null;
		}
	}

	@Override
	public IconDescriptor getDescriptor() {
		if (iconSet != null) {
			return new IconDescriptor(getId(), getIconSetId(), getIconSetLabel(), getLabel(), bytes);
		}
		else {
			return null;
		}
	}
}
