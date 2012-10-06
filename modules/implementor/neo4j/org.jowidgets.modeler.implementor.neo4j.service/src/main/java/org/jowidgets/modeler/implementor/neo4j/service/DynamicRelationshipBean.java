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

package org.jowidgets.modeler.implementor.neo4j.service;

import org.jowidgets.cap.service.neo4j.tools.BeanPropertyMapRelationshipBean;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

public class DynamicRelationshipBean extends BeanPropertyMapRelationshipBean {

	public static final String SOURCE_ID_PROPERTY_PREFIX = "SourceId";
	public static final String DESTINATION_ID_PROPERTY_PREFIX = "DestinationId";

	public DynamicRelationshipBean(final Relationship relationship) {
		super(relationship);
	}

	@Override
	public void setProperty(final String propertyName, final Object value) {
		if (propertyName.startsWith(SOURCE_ID_PROPERTY_PREFIX + ":")) {
			final String[] split = propertyName.split(":");
			setStartNodeId(split[1], new RelationshipTypeImpl(split[2]), split[2], value);
		}
		else if (propertyName.startsWith(DESTINATION_ID_PROPERTY_PREFIX + ":")) {
			final String[] split = propertyName.split(":");
			setEndNodeId(split[1], new RelationshipTypeImpl(split[2]), split[2], value);
		}
		else {
			super.setProperty(propertyName, value);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <RESULT_TYPE> RESULT_TYPE getProperty(final String propertyName) {
		if (propertyName.startsWith(SOURCE_ID_PROPERTY_PREFIX + ":")) {
			return (RESULT_TYPE) getStartNodeId();
		}
		else if (propertyName.startsWith(DESTINATION_ID_PROPERTY_PREFIX + ":")) {
			return (RESULT_TYPE) getEndNodeId();
		}
		else {
			return super.getProperty(propertyName);
		}
	}

	private static final class RelationshipTypeImpl implements RelationshipType {

		private final String name;

		private RelationshipTypeImpl(final String name) {
			this.name = name;
		}

		@Override
		public String name() {
			return name;
		}

	}

}
