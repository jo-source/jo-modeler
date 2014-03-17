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

package org.jowidgets.modeler.ui.plugins;

import org.jowidgets.cap.addons.plugins.beanform.document.api.DocumentBeanFormPluginFactory;
import org.jowidgets.cap.common.api.bean.IBeanDto;
import org.jowidgets.cap.ui.api.plugin.IAttributePlugin;
import org.jowidgets.cap.ui.api.plugin.IBeanFormPlugin;
import org.jowidgets.cap.ui.api.plugin.IBeanProxyLabelRendererPlugin;
import org.jowidgets.cap.ui.api.plugin.IBeanProxyPlugin;
import org.jowidgets.cap.ui.api.plugin.IBeanTableMenuContributionPlugin;
import org.jowidgets.cap.ui.api.plugin.IBeanTableMenuInterceptorPlugin;
import org.jowidgets.cap.ui.api.plugin.IEntityComponentMasterTableViewPlugin;
import org.jowidgets.cap.ui.api.plugin.IEntityComponentRelationTreeDetailViewPlugin;
import org.jowidgets.cap.ui.api.plugin.IEntityComponentRelationTreeViewPlugin;
import org.jowidgets.cap.ui.tools.plugin.BeanMessagePopupPlugin;
import org.jowidgets.cap.ui.tools.workbench.EntityComponentMasterTableToolBarPlugin;
import org.jowidgets.cap.ui.tools.workbench.EntityComponentRelationTreeDetailToolBarPlugin;
import org.jowidgets.cap.ui.tools.workbench.EntityComponentRelationTreeToolBarPlugin;
import org.jowidgets.modeler.common.bean.IEntityModel;
import org.jowidgets.modeler.common.bean.IEntityPropertyModel;
import org.jowidgets.modeler.common.bean.IIcon;
import org.jowidgets.modeler.common.bean.IIconSet;
import org.jowidgets.modeler.common.bean.ILookUpElement;
import org.jowidgets.modeler.ui.plugins.attribute.EntityModelAttributesPlugin;
import org.jowidgets.modeler.ui.plugins.attribute.GlobalModelerAttributesPlugin;
import org.jowidgets.modeler.ui.plugins.attribute.IconAttributesPlugin;
import org.jowidgets.modeler.ui.plugins.bean.EntityModelRendererPlugin;
import org.jowidgets.modeler.ui.plugins.bean.IconRendererPlugin;
import org.jowidgets.modeler.ui.plugins.bean.LookUpElementRendererPlugin;
import org.jowidgets.modeler.ui.plugins.table.IconSetMenuContributionPlugin;
import org.jowidgets.modeler.ui.plugins.table.PropertyModelMenuContributionPlugin;
import org.jowidgets.modeler.ui.plugins.table.PropertyModelMenuInterceptorPlugin;
import org.jowidgets.plugin.tools.PluginProviderBuilder;
import org.jowidgets.plugin.tools.PluginProviderHolder;

public final class ModelerPluginProviderHolder extends PluginProviderHolder {

	public ModelerPluginProviderHolder() {
		super(new ModelerPluginProviderBuilder(), 2);
	}

	private static final class ModelerPluginProviderBuilder extends PluginProviderBuilder {

		public ModelerPluginProviderBuilder() {
			addPlugin(IAttributePlugin.ID, new GlobalModelerAttributesPlugin());

			addPlugin(IEntityComponentMasterTableViewPlugin.ID, new EntityComponentMasterTableToolBarPlugin());
			addPlugin(IEntityComponentRelationTreeViewPlugin.ID, new EntityComponentRelationTreeToolBarPlugin());
			addPlugin(IEntityComponentRelationTreeDetailViewPlugin.ID, new EntityComponentRelationTreeDetailToolBarPlugin());

			addBeanProxyRendererPlugin(new EntityModelRendererPlugin(), IEntityModel.class);
			addBeanProxyRendererPlugin(new LookUpElementRendererPlugin(), ILookUpElement.class);
			addBeanProxyRendererPlugin(new IconRendererPlugin(), IIcon.class);

			addBeanTableMenuContributionPlugin(new PropertyModelMenuContributionPlugin(), IEntityPropertyModel.class);
			addBeanTableMenuContributionPlugin(new IconSetMenuContributionPlugin(), IIconSet.class);

			addBeanTableMenuInterceptorPlugin(new PropertyModelMenuInterceptorPlugin(), IEntityPropertyModel.class);

			addAttributesPlugin(new IconAttributesPlugin(), IIcon.class);
			addAttributesPlugin(new EntityModelAttributesPlugin(), IEntityModel.class);

			addBeanFormPlugin(DocumentBeanFormPluginFactory.create(), IBeanDto.class);

			addPlugin(IBeanProxyPlugin.ID, new BeanMessagePopupPlugin());
		}

		private void addBeanTableMenuContributionPlugin(
			final IBeanTableMenuContributionPlugin<?> plugin,
			final Class<?>... beanTypes) {
			addPlugin(
					IBeanTableMenuContributionPlugin.ID,
					plugin,
					IBeanTableMenuContributionPlugin.BEAN_TYPE_PROPERTY_KEY,
					beanTypes);
		}

		private void addBeanTableMenuInterceptorPlugin(
			final IBeanTableMenuInterceptorPlugin<?> plugin,
			final Class<?>... beanTypes) {
			addPlugin(
					IBeanTableMenuInterceptorPlugin.ID,
					plugin,
					IBeanTableMenuInterceptorPlugin.BEAN_TYPE_PROPERTY_KEY,
					beanTypes);
		}

		private void addAttributesPlugin(final IAttributePlugin plugin, final Class<?>... beanTypes) {
			addPlugin(IAttributePlugin.ID, plugin, IAttributePlugin.BEAN_TYPE_PROPERTY_KEY, beanTypes);
		}

		private void addBeanProxyRendererPlugin(final IBeanProxyLabelRendererPlugin<?> plugin, final Class<?> beanType) {
			addPlugin(IBeanProxyLabelRendererPlugin.ID, plugin, IBeanProxyLabelRendererPlugin.BEAN_TYPE_PROPERTY_KEY, beanType);
		}

		private void addBeanFormPlugin(final IBeanFormPlugin plugin, final Class<?>... beanTypes) {
			addPlugin(IBeanFormPlugin.ID, plugin, IBeanFormPlugin.BEAN_TYPE_PROPERTY_KEY, beanTypes);
		}

	}
}
