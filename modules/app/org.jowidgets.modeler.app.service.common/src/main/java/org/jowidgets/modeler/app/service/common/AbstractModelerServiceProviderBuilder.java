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

package org.jowidgets.modeler.app.service.common;

import org.jowidgets.cap.common.api.bean.IBean;
import org.jowidgets.cap.common.api.execution.IExecutableChecker;
import org.jowidgets.cap.common.api.service.EntityServiceComposite;
import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.common.api.service.IEntityServiceCompositeBuilder;
import org.jowidgets.cap.common.api.service.IExecutorService;
import org.jowidgets.cap.common.api.service.ILookUpService;
import org.jowidgets.cap.common.api.service.IPasswordChangeService;
import org.jowidgets.cap.security.service.tools.DefaultAuthorizationProviderService;
import org.jowidgets.cap.service.api.bean.IBeanAccess;
import org.jowidgets.cap.service.api.executor.IBeanListExecutor;
import org.jowidgets.cap.service.hibernate.api.HibernateServiceToolkit;
import org.jowidgets.cap.service.jpa.api.IJpaServicesDecoratorProviderBuilder;
import org.jowidgets.cap.service.jpa.api.JpaServiceToolkit;
import org.jowidgets.cap.service.tools.CapServiceProviderBuilder;
import org.jowidgets.modeler.common.bean.IEntityPropertyModel;
import org.jowidgets.modeler.common.checker.MovePropertiesUpExecutableChecker;
import org.jowidgets.modeler.common.executor.ModelerExecutorServices;
import org.jowidgets.modeler.common.lookup.LookUpIds;
import org.jowidgets.modeler.common.service.IIconCreatorService;
import org.jowidgets.modeler.service.entity.ModelerEntityServiceBuilder;
import org.jowidgets.modeler.service.executor.MovePropertiesDownExecutor;
import org.jowidgets.modeler.service.executor.MovePropertiesUpExecutor;
import org.jowidgets.modeler.service.icons.IconsCreatorServiceImpl;
import org.jowidgets.modeler.service.lookup.CardinalityLookUpService;
import org.jowidgets.modeler.service.lookup.EntityModelsLookUpService;
import org.jowidgets.modeler.service.lookup.IconSetsLookUpService;
import org.jowidgets.modeler.service.lookup.LookUpDisplayFormatLookUpService;
import org.jowidgets.modeler.service.lookup.ValueTypeLookUpService;
import org.jowidgets.modeler.service.persistence.ModelerPersistenceUnitNames;
import org.jowidgets.modeler.service.persistence.bean.EntityPropertyModel;
import org.jowidgets.service.api.IServiceId;
import org.jowidgets.service.api.IServicesDecoratorProvider;
import org.jowidgets.useradmin.common.security.AuthorizationProviderServiceId;
import org.jowidgets.useradmin.service.entity.UserAdminEntityServiceBuilder;
import org.jowidgets.useradmin.service.password.PasswordChangeServiceImpl;

public abstract class AbstractModelerServiceProviderBuilder extends CapServiceProviderBuilder {

	public AbstractModelerServiceProviderBuilder() {
		addService(AuthorizationProviderServiceId.ID, new DefaultAuthorizationProviderService<String>());
		addService(IPasswordChangeService.ID, new PasswordChangeServiceImpl(ModelerPersistenceUnitNames.MODELER));

		addService(IEntityService.ID, createEntityService());

		addService(IIconCreatorService.ID, new IconsCreatorServiceImpl());

		addLookUpService(LookUpIds.VALUE_TYPES, new ValueTypeLookUpService());
		addLookUpService(LookUpIds.ENTITY_MODELS, new EntityModelsLookUpService());
		addLookUpService(LookUpIds.ICONS_SETS, new IconSetsLookUpService());
		addLookUpService(LookUpIds.CARDINALITY, new CardinalityLookUpService());
		addLookUpService(LookUpIds.LOOK_UP_DISPLAY_FORMAT, new LookUpDisplayFormatLookUpService());

		addEntityPropertyExecutorService(
				ModelerExecutorServices.MOVE_ENTITY_PROPERTIES_UP,
				new MovePropertiesUpExecutor(),
				new MovePropertiesUpExecutableChecker());
		addEntityPropertyExecutorService(ModelerExecutorServices.MOVE_ENTITY_PROPERTIES_DOWN, new MovePropertiesDownExecutor());

		addServiceDecorator(createJpaServiceDecoratorProvider());
		addServiceDecorator(createCancelServiceDecoratorProvider());
	}

	private IEntityService createEntityService() {
		final IEntityServiceCompositeBuilder builder = EntityServiceComposite.builder();
		builder.add(new ModelerEntityServiceBuilder(this).build());
		builder.add(new UserAdminEntityServiceBuilder(this).build());
		return builder.build();
	}

	private IServicesDecoratorProvider createJpaServiceDecoratorProvider() {
		final IJpaServicesDecoratorProviderBuilder builder = JpaServiceToolkit.serviceDecoratorProviderBuilder(ModelerPersistenceUnitNames.MODELER);
		builder.addEntityManagerServices(ILookUpService.class);
		builder.addEntityManagerServices(IIconCreatorService.class);
		builder.addTransactionalServices(IIconCreatorService.class);
		builder.addExceptionDecorator(HibernateServiceToolkit.exceptionDecorator());
		onCreateJpaServiceDecoratorProvider(builder);
		return builder.build();
	}

	private IServicesDecoratorProvider createCancelServiceDecoratorProvider() {
		return HibernateServiceToolkit.cancelServiceDecoratorProviderBuilder(ModelerPersistenceUnitNames.MODELER).build();
	}

	protected void onCreateJpaServiceDecoratorProvider(final IJpaServicesDecoratorProviderBuilder builder) {}

	private <BEAN_TYPE extends IBean, PARAM_TYPE> void addEntityPropertyExecutorService(
		final IServiceId<? extends IExecutorService<PARAM_TYPE>> id,
		final IBeanListExecutor<? extends BEAN_TYPE, PARAM_TYPE> beanExecutor) {
		addEntityPropertyExecutorService(id, beanExecutor, null);
	}

	private <BEAN_TYPE extends IBean, PARAM_TYPE> void addEntityPropertyExecutorService(
		final IServiceId<? extends IExecutorService<PARAM_TYPE>> id,
		final IBeanListExecutor<? extends BEAN_TYPE, PARAM_TYPE> beanExecutor,
		final IExecutableChecker<? extends BEAN_TYPE> executableChecker) {
		final IBeanAccess<EntityPropertyModel> beanAccess = JpaServiceToolkit.serviceFactory().beanAccess(
				EntityPropertyModel.class);
		addExecutorService(id, beanExecutor, executableChecker, beanAccess, IEntityPropertyModel.ALL_PROPERTIES);
	}
}
