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

package org.jowidgets.modeler.common.dto;

import java.io.Serializable;

import org.jowidgets.util.Assert;

public final class IconDescriptor implements Serializable {

	private static final long serialVersionUID = -3991451221949813787L;

	private final Long iconId;
	private final Object iconSetId;
	private final String iconSetLabel;
	private final String iconLabel;
	private final byte[] bytes;

	public IconDescriptor(
		final Long iconId,
		final Object iconSetId,
		final String iconSetLabel,
		final String iconLabel,
		final byte[] bytes) {
		Assert.paramNotNull(iconId, "iconId");
		Assert.paramNotNull(iconSetId, "iconSetId");
		Assert.paramNotEmpty(iconSetLabel, "iconSetLabel");
		Assert.paramNotEmpty(iconLabel, "iconLabel");
		Assert.paramNotNull(bytes, "bytes");
		this.iconId = iconId;
		this.iconSetId = iconSetId;
		this.iconSetLabel = iconSetLabel;
		this.iconLabel = iconLabel;
		this.bytes = bytes;
	}

	public Long getIconId() {
		return iconId;
	}

	public Object getIconSetId() {
		return iconSetId;
	}

	public String getIconSetLabel() {
		return iconSetLabel;
	}

	public String getIconLabel() {
		return iconLabel;
	}

	public byte[] getBytes() {
		return bytes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iconId == null) ? 0 : iconId.hashCode());
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof IconDescriptor)) {
			return false;
		}
		final IconDescriptor other = (IconDescriptor) obj;
		if (iconId == null) {
			if (other.iconId != null) {
				return false;
			}
		}
		else if (!iconId.equals(other.iconId)) {
			return false;
		}
		return true;
	}

}
