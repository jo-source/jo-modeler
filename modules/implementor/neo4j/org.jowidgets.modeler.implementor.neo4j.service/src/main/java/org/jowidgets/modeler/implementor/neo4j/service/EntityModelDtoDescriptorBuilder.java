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

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import org.jowidgets.cap.common.api.CapCommonToolkit;
import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.bean.IBeanDtoDescriptor;
import org.jowidgets.cap.common.api.bean.IBeanPropertyBuilder;
import org.jowidgets.cap.common.api.bean.IProperty;
import org.jowidgets.cap.common.api.bean.IPropertyBuilder;
import org.jowidgets.cap.common.api.sort.ISort;
import org.jowidgets.cap.common.api.validation.IBeanValidator;
import org.jowidgets.cap.service.neo4j.api.GraphDBConfig;
import org.jowidgets.i18n.api.IMessage;
import org.jowidgets.modeler.common.i18n.entity.ModelerEntityMessages;
import org.jowidgets.modeler.service.persistence.bean.AbstractPropertyModel;
import org.jowidgets.modeler.service.persistence.bean.EntityModel;
import org.jowidgets.modeler.service.persistence.bean.EntityPropertyModel;
import org.jowidgets.util.Assert;

final class EntityModelDtoDescriptorBuilder {

	private EntityModelDtoDescriptorBuilder() {}

	static IBeanDtoDescriptor create(final EntityModel entityModel) {

		final Collection<IProperty> properties = createProperties(entityModel);
		final Collection<ISort> defaultSorting = Collections.emptyList();
		final Collection<IBeanValidator<?>> beanValidators = Collections.emptyList();

		return CapCommonToolkit.dtoDescriptor(
				properties,
				defaultSorting,
				entityModel.getLabelSingular(),
				entityModel.getLabelPlural(),
				null,
				entityModel.getRenderingPattern(),
				beanValidators);
	}

	private static Collection<IProperty> createProperties(final EntityModel entityModel) {
		final Collection<IProperty> result = new LinkedList<IProperty>();

		result.add(createIdProperty());
		final List<EntityPropertyModel> properties = new LinkedList<EntityPropertyModel>(entityModel.getEntityPropertyModels());
		Collections.sort(properties);
		for (final EntityPropertyModel property : properties) {
			result.add(createProperty(property));
		}
		result.add(createVersionProperty());
		result.add(createBeanTypeProperty());
		return result;
	}

	private static IProperty createProperty(final AbstractPropertyModel propertyModel) {
		final IPropertyBuilder builder = CapCommonToolkit.propertyBuilder();
		builder.setName(propertyModel.getName());
		builder.setValueType(getValueType(propertyModel.getValueType()));
		builder.setLabel(propertyModel.getLabel());
		builder.setLabelLong(propertyModel.getLabelLong());
		builder.setDescription(propertyModel.getDescription());
		builder.setEditable(propertyModel.getEditable());
		builder.setReadonly(!propertyModel.getEditable());
		builder.setSearchable(propertyModel.getSearchable());
		builder.setMandatory(propertyModel.getMandatory());
		builder.setFilterable(true);
		builder.setSortable(true);
		return builder.build();
	}

	private static Class<?> getValueType(final String valueType) {
		if (String.class.getName().equals(valueType)) {
			return String.class;
		}
		else if (Long.class.getName().equals(valueType)) {
			return Long.class;
		}
		else if (Integer.class.getName().equals(valueType)) {
			return Integer.class;
		}
		else if (Double.class.getName().equals(valueType)) {
			return Double.class;
		}
		else if (Date.class.getName().equals(valueType)) {
			return Date.class;
		}
		else if (Boolean.class.getName().equals(valueType)) {
			return Boolean.class;
		}
		else {
			throw new IllegalArgumentException("Value type '" + valueType + "' is not yet supoorted.");
		}
	}

	private static IProperty createIdProperty() {
		final IBeanPropertyBuilder builder = CapCommonToolkit.beanPropertyBuilder(IBean.class, IBean.ID_PROPERTY);
		builder.setLabel(getMessage("id.label"));
		builder.setDescription(getMessage("id.description"));
		builder.setSortable(true);
		builder.setVisible(false);
		return builder.build();
	}

	private static IProperty createVersionProperty() {
		final IBeanPropertyBuilder builder = CapCommonToolkit.beanPropertyBuilder(IBean.class, IBean.VERSION_PROPERTY);
		builder.setLabel(getMessage("version.label"));
		builder.setDescription(getMessage("version.description"));
		builder.setVisible(false);
		return builder.build();
	}

	private static IProperty createBeanTypeProperty() {
		final IPropertyBuilder builder = CapCommonToolkit.propertyBuilder();
		builder.setName(GraphDBConfig.getBeanTypePropertyName());
		builder.setValueType(String.class);
		//TODO MG i18n
		builder.setLabel("Type");
		builder.setDescription("The type of the dataset");
		builder.setVisible(false);
		return builder.build();
	}

	private static IMessage getMessage(final String keySuffix) {
		Assert.paramNotEmpty(keySuffix, "keySuffix");
		return ModelerEntityMessages.getMessage("AbstractDtoDescriptorBuilder." + keySuffix);
	}
}
