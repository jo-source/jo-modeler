/*
 * Copyright (c) 2013, grossmann
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

package org.jowidgets.modeler.implementor.neo4j.service;

import java.util.Date;
import java.util.Map;

import org.jowidgets.cap.common.api.dto.IDocument;
import org.jowidgets.cap.common.tools.dto.Document;
import org.jowidgets.cap.service.neo4j.tools.BeanPropertyMapNodeBean;
import org.neo4j.graphdb.Node;

class Neo4JImplementorBeanPropertyMapNodeBean extends BeanPropertyMapNodeBean {

	private final Map<String, String> propertyTypes;

	Neo4JImplementorBeanPropertyMapNodeBean(final Node node, final Map<String, String> propertyTypes) {
		super(node);
		this.propertyTypes = propertyTypes;
	}

	@Override
	public void setValue(final String propertyName, final Object value) {
		final String propertyType = propertyTypes.get(propertyName);
		if (Date.class.getName().equals(propertyType)) {
			super.setDateProperty(propertyName, (java.util.Date) value);
		}
		else if (IDocument.class.getName().equals(propertyType)) {
			final IDocument document = (IDocument) value;
			if (document != null) {
				super.setProperty(propertyName, document.getUrl());
			}
			else {
				super.setValue(propertyName, value);
			}
		}
		else {
			super.setValue(propertyName, value);
		}
	}

	@Override
	public Object getValue(final String propertyName) {
		final String propertyType = propertyTypes.get(propertyName);
		if (Date.class.getName().equals(propertyType)) {
			return super.getDateProperty(propertyName);
		}
		else if (IDocument.class.getName().equals(propertyType)) {
			final Object value = super.getValue(propertyName);
			if (value instanceof String) {
				return new Document((String) value);
			}
			else {
				return super.getValue(propertyName);
			}
		}
		else {
			return super.getValue(propertyName);
		}
	}

}
