/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.github.chensheng.dddboot.nacos.spring.env;

import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import io.github.chensheng.dddboot.nacos.spring.annotation.ConfigServiceBeanBuilder;
import io.github.chensheng.dddboot.nacos.spring.context.event.config.EventPublishingConfigService;
import io.github.chensheng.dddboot.nacos.spring.factory.NacosServiceFactory;
import io.github.chensheng.dddboot.nacos.spring.util.BeanUtils;
import io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils;
import io.github.chensheng.dddboot.nacos.spring.util.NacosUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ConfigurationClassPostProcessor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.*;

import static io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils.getNacosServiceFactoryBean;
import static org.springframework.util.ObjectUtils.nullSafeEquals;

/**
 * {@link BeanFactoryPostProcessor Post Processor} resolves
 * {@link  io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource @NacosPropertySource}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see  io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource
 * @see PropertySource
 * @see BeanDefinitionRegistryPostProcessor
 * @since 0.1.0
 */
public class NacosPropertySourcePostProcessor
		implements BeanDefinitionRegistryPostProcessor, BeanFactoryPostProcessor,
		EnvironmentAware, Ordered {

	public static final String BEAN_NAME = "nacosPropertySourcePostProcessor";

	private static BeanFactory beanFactory;

	private final Set<String> processedBeanNames = new LinkedHashSet<String>();

	private ConfigurableEnvironment environment;

	private Collection<AbstractNacosPropertySourceBuilder> nacosPropertySourceBuilders;

	private ConfigServiceBeanBuilder configServiceBeanBuilder;

	public static void addListenerIfAutoRefreshed(
			final NacosPropertySource nacosPropertySource, final Properties properties,
			final ConfigurableEnvironment environment) {

		if (!nacosPropertySource.isAutoRefreshed()) { // Disable Auto-Refreshed
			return;
		}

		final String dataId = nacosPropertySource.getDataId();
		final String groupId = nacosPropertySource.getGroupId();
		final String type = nacosPropertySource.getType();
		final NacosServiceFactory nacosServiceFactory = NacosBeanUtils.getNacosServiceFactoryBean(
				beanFactory);

		try {

			ConfigService configService = nacosServiceFactory
					.createConfigService(properties);

			Listener listener = new AbstractListener() {

				@Override
				public void receiveConfigInfo(String config) {
					String name = nacosPropertySource.getName();
					NacosPropertySource newNacosPropertySource = new NacosPropertySource(
							dataId, groupId, name, config, type);
					newNacosPropertySource.copy(nacosPropertySource);
					MutablePropertySources propertySources = environment
							.getPropertySources();
					// replace NacosPropertySource
					propertySources.replace(name, newNacosPropertySource);
				}
			};

			if (configService instanceof EventPublishingConfigService) {
				((EventPublishingConfigService) configService).addListener(dataId,
						groupId, type, listener);
			}
			else {
				configService.addListener(dataId, groupId, listener);
			}

		}
		catch (NacosException e) {
			throw new RuntimeException(
					"ConfigService can't add Listener with properties : " + properties,
					e);
		}
	}

	@Override
	public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry)
			throws BeansException {
	}

	@Override
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory)
			throws BeansException {
		String[] abstractNacosPropertySourceBuilderBeanNames = BeanUtils
				.getBeanNames(beanFactory, AbstractNacosPropertySourceBuilder.class);

		this.nacosPropertySourceBuilders = new ArrayList<AbstractNacosPropertySourceBuilder>(
				abstractNacosPropertySourceBuilderBeanNames.length);

		for (String beanName : abstractNacosPropertySourceBuilderBeanNames) {
			this.nacosPropertySourceBuilders.add(beanFactory.getBean(beanName,
					AbstractNacosPropertySourceBuilder.class));
		}

		NacosPropertySourcePostProcessor.beanFactory = beanFactory;
		this.configServiceBeanBuilder = NacosBeanUtils.getConfigServiceBeanBuilder(beanFactory);

		String[] beanNames = beanFactory.getBeanDefinitionNames();

		for (String beanName : beanNames) {
			processPropertySource(beanName, beanFactory);
		}

	}

	private void processPropertySource(String beanName,
			ConfigurableListableBeanFactory beanFactory) {

		if (processedBeanNames.contains(beanName)) {
			return;
		}

		BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);

		// Build multiple instance if possible
		List<NacosPropertySource> nacosPropertySources = buildNacosPropertySources(
				beanName, beanDefinition);

		// Add Orderly
		for (NacosPropertySource nacosPropertySource : nacosPropertySources) {
			addNacosPropertySource(nacosPropertySource);
			Properties properties = configServiceBeanBuilder
					.resolveProperties(nacosPropertySource.getAttributesMetadata());
			addListenerIfAutoRefreshed(nacosPropertySource, properties, environment);
		}

		processedBeanNames.add(beanName);
	}

	private List<NacosPropertySource> buildNacosPropertySources(String beanName,
			BeanDefinition beanDefinition) {
		for (AbstractNacosPropertySourceBuilder builder : nacosPropertySourceBuilders) {
			if (builder.supports(beanDefinition)) {
				return builder.build(beanName, beanDefinition);
			}
		}
		return Collections.emptyList();
	}

	private void addNacosPropertySource(NacosPropertySource nacosPropertySource) {

		MutablePropertySources propertySources = environment.getPropertySources();

		boolean first = nacosPropertySource.isFirst();
		String before = nacosPropertySource.getBefore();
		String after = nacosPropertySource.getAfter();

		boolean hasBefore = !nullSafeEquals(NacosUtils.DEFAULT_STRING_ATTRIBUTE_VALUE, before);
		boolean hasAfter = !nullSafeEquals(NacosUtils.DEFAULT_STRING_ATTRIBUTE_VALUE, after);

		boolean isRelative = hasBefore || hasAfter;

		if (first) { // If First
			propertySources.addFirst(nacosPropertySource);
		}
		else if (isRelative) { // If relative
			if (hasBefore) {
				propertySources.addBefore(before, nacosPropertySource);
			}
			if (hasAfter) {
				propertySources.addAfter(after, nacosPropertySource);
			}
		}
		else {
			propertySources.addLast(nacosPropertySource); // default add last
		}
	}

	/**
	 * The order is closed to {@link ConfigurationClassPostProcessor#getOrder()
	 * HIGHEST_PRECEDENCE} almost.
	 *
	 * @return <code>Ordered.HIGHEST_PRECEDENCE + 1</code>
	 * @see ConfigurationClassPostProcessor#getOrder()
	 */
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE + 1;
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = (ConfigurableEnvironment) environment;
	}

}
