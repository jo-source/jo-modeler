/*
 * Copyright (c) 2011, grossmann
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

package org.jowidgets.modeler.common.security;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public final class ModelerAuthKeys {

	//CRUD services
	public static final String CREATE_ENTITY_MODEL = "CREATE_ENTITY_MODEL";
	public static final String READ_ENTITY_MODEL = "READ_ENTITY_MODEL";
	public static final String UPDATE_ENTITY_MODEL = "UPDATE_ENTITY_MODEL";
	public static final String DELETE_ENTITY_MODEL = "DELETE_ENTITY_MODEL";

	public static final String CREATE_PROPERTY_MODEL = "CREATE_PROPERTY_MODEL";
	public static final String READ_PROPERTY_MODEL = "READ_PROPERTY_MODEL";
	public static final String UPDATE_PROPERTY_MODEL = "UPDATE_PROPERTY_MODEL";
	public static final String DELETE_PROPERTY_MODEL = "DELETE_PROPERTY_MODEL";

	public static final String CREATE_ENTITY_MODEL_PROPERTY_MODEL_LINK = "CREATE_ENTITY_MODEL_PROPERTY_MODEL_LINK";
	public static final String READ_ENTITY_MODEL_PROPERTY_MODEL_LINK = "READ_ENTITY_MODEL_PROPERTY_MODEL_LINK";
	public static final String UPDATE_ENTITY_MODEL_PROPERTY_MODEL_LINK = "UPDATE_ENTITY_MODEL_PROPERTY_MODEL_LINK";
	public static final String DELETE_ENTITY_MODEL_PROPERTY_MODEL_LINK = "DELETE_ENTITY_MODEL_PROPERTY_MODEL_LINK";

	public static final String CREATE_RELATION_MODEL = "CREATE_RELATION_MODEL";
	public static final String READ_RELATION_MODEL = "READ_RELATION_MODEL";
	public static final String UPDATE_RELATION_MODEL = "UPDATE_RELATION_MODEL";
	public static final String DELETE_RELATION_MODEL = "DELETE_RELATION_MODEL";

	//Authorizations collection
	public static final Collection<String> ALL_AUTHORIZATIONS = createAuthorizations();

	private ModelerAuthKeys() {}

	private static List<String> createAuthorizations() {
		final List<String> result = new LinkedList<String>();
		for (final Field field : ModelerAuthKeys.class.getDeclaredFields()) {
			if (field.getType().equals(String.class)) {
				try {
					result.add((String) field.get(ModelerAuthKeys.class));
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}

}
