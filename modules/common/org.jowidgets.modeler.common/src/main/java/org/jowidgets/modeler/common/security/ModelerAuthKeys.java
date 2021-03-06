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

	public static final String MODELER_ADMIN_GROUP = "MODELER_ADMIN";

	//Executor services
	public static final String EXECUTOR_MOVE_PROPERTIES_UP = "MODELER_EXECUTOR_MOVE_PROPERTIES_UP";
	public static final String EXECUTOR_MOVE_PROPERTIES_DOWN = "MODELER_EXECUTOR_MOVE_PROPERTIES_DOWN";

	//CRUD services
	public static final String CREATE_ENTITY_MODEL = "MODELER_CREATE_ENTITY_MODEL";
	public static final String READ_ENTITY_MODEL = "MODELER_READ_ENTITY_MODEL";
	public static final String UPDATE_ENTITY_MODEL = "MODELER_UPDATE_ENTITY_MODEL";
	public static final String DELETE_ENTITY_MODEL = "MODELER_DELETE_ENTITY_MODEL";

	public static final String CREATE_ENTITY_PROPERTY_MODEL = "MODELER_CREATE_ENTITY_PROPERTY_MODEL";
	public static final String READ_ENTITY_PROPERTY_MODEL = "MODELER_READ_ENTITY_PROPERTY_MODEL";
	public static final String UPDATE_ENTITY_PROPERTY_MODEL = "MODELER_UPDATE_ENTITY_PROPERTY_MODEL";
	public static final String DELETE_ENTITY_PROPERTY_MODEL = "MODELER_DELETE_ENTITY_PROPERTY_MODEL";

	public static final String CREATE_RELATION_MODEL = "MODELER_CREATE_RELATION_MODEL";
	public static final String READ_RELATION_MODEL = "MODELER_READ_RELATION_MODEL";
	public static final String UPDATE_RELATION_MODEL = "MODELER_UPDATE_RELATION_MODEL";
	public static final String DELETE_RELATION_MODEL = "MODELER_DELETE_RELATION_MODEL";

	public static final String CREATE_LOOK_UP = "MODELER_CREATE_LOOK_UP";
	public static final String READ_LOOK_UP = "MODELER_READ_LOOK_UP";
	public static final String UPDATE_LOOK_UP = "MODELER_UPDATE_LOOK_UP";
	public static final String DELETE_LOOK_UP = "MODELER_DELETE_LOOK_UP";

	public static final String CREATE_LOOK_UP_ELEMENT = "MODELER_CREATE_LOOK_UP_ELEMENT";
	public static final String READ_LOOK_UP_ELEMENT = "MODELER_READ_LOOK_UP_ELEMENT";
	public static final String UPDATE_LOOK_UP_ELEMENT = "MODELER_UPDATE_LOOK_UP_ELEMENT";
	public static final String DELETE_LOOK_UP_ELEMENT = "MODELER_DELETE_LOOK_UP_ELEMENT";

	public static final String CREATE_ICON_SET = "MODELER_CREATE_ICON_SET";
	public static final String READ_ICON_SET = "MODELER_READ_ICON_SET";
	public static final String UPDATE_ICON_SET = "MODELER_UPDATE_ICON_SET";
	public static final String DELETE_ICON_SET = "MODELER_DELETE_ICON_SET";

	public static final String CREATE_ICON = "MODELER_CREATE_ICON";
	public static final String READ_ICON = "MODELER_READ_ICON";
	public static final String UPDATE_ICON = "MODELER_UPDATE_ICON";
	public static final String DELETE_ICON = "MODELER_DELETE_ICON";

	//Authorizations collection
	public static final Collection<String> ALL_AUTHORIZATIONS = createAuthorizations();

	private ModelerAuthKeys() {}

	private static List<String> createAuthorizations() {
		final List<String> result = new LinkedList<String>();
		for (final Field field : ModelerAuthKeys.class.getDeclaredFields()) {
			if (field.getType().equals(String.class)) {
				try {
					final String authorizationKey = (String) field.get(ModelerAuthKeys.class);
					if (!MODELER_ADMIN_GROUP.equals(authorizationKey)) {
						result.add(authorizationKey);
					}
				}
				catch (final Exception e) {
					throw new RuntimeException(e);
				}
			}
		}
		return result;
	}

}
