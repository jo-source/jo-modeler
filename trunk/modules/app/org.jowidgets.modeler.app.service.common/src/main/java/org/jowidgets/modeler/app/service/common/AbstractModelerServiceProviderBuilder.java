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

package org.jowidgets.modeler.app.service.common;

import org.jowidgets.cap.common.api.service.IEntityService;
import org.jowidgets.cap.common.api.service.IPasswordChangeService;
import org.jowidgets.cap.security.service.tools.DefaultAuthorizationProviderService;
import org.jowidgets.cap.service.api.entity.EntityServiceComposite;
import org.jowidgets.cap.service.api.entity.IEntityServiceCompositeBuilder;
import org.jowidgets.cap.service.hibernate.api.HibernateServiceToolkit;
import org.jowidgets.cap.service.jpa.api.IJpaServicesDecoratorProviderBuilder;
import org.jowidgets.cap.service.jpa.api.JpaServiceToolkit;
import org.jowidgets.cap.service.tools.CapServiceProviderBuilder;
import org.jowidgets.modeler.service.entity.ModelerEntityServiceBuilder;
import org.jowidgets.service.api.IServicesDecoratorProvider;
import org.jowidgets.useradmin.common.security.AuthorizationProviderServiceId;
import org.jowidgets.useradmin.service.entity.UserAdminEntityServiceBuilder;
import org.jowidgets.useradmin.service.password.PasswordChangeServiceImpl;
import org.jowidgets.useradmin.service.persistence.UseradminPersistenceUnitNames;

public abstract class AbstractModelerServiceProviderBuilder extends CapServiceProviderBuilder {

	public AbstractModelerServiceProviderBuilder() {
		addService(AuthorizationProviderServiceId.ID, new DefaultAuthorizationProviderService<String>());
		addService(IPasswordChangeService.ID, new PasswordChangeServiceImpl());

		addService(IEntityService.ID, createEntityService());

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
		final IJpaServicesDecoratorProviderBuilder builder = JpaServiceToolkit.serviceDecoratorProviderBuilder(UseradminPersistenceUnitNames.USER_ADMIN);
		builder.addExceptionDecorator(HibernateServiceToolkit.exceptionDecorator());
		onCreateJpaServiceDecoratorProvider(builder);
		return builder.build();
	}

	private IServicesDecoratorProvider createCancelServiceDecoratorProvider() {
		return HibernateServiceToolkit.serviceDecoratorProviderBuilder(UseradminPersistenceUnitNames.USER_ADMIN).build();
	}

	protected void onCreateJpaServiceDecoratorProvider(final IJpaServicesDecoratorProviderBuilder builder) {}
}
