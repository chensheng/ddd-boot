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
import com.alibaba.nacos.api.config.ConfigType;
import io.github.chensheng.dddboot.nacos.spring.context.event.DeferredApplicationEventPublisher;
import io.github.chensheng.dddboot.nacos.spring.context.event.config.NacosConfigMetadataEvent;
import io.github.chensheng.dddboot.nacos.spring.util.NacosUtils;
import io.github.chensheng.dddboot.nacos.spring.util.config.NacosConfigLoader;
import io.github.chensheng.dddboot.nacos.spring.util.GlobalNacosPropertiesSource;
import io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.*;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.*;

import static io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils.getNacosServiceFactoryBean;
import static io.github.chensheng.dddboot.nacos.spring.util.NacosUtils.*;
import static java.lang.String.format;
import static org.springframework.util.ClassUtils.resolveClassName;

/**
 * Abstract implementation of {@link NacosPropertySource} Builder
 *
 * @param <T> The type of {@link BeanDefinition}
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 0.1.0
 */
public abstract class AbstractNacosPropertySourceBuilder<T extends BeanDefinition>
		implements EnvironmentAware, BeanFactoryAware, BeanClassLoaderAware,
		ApplicationContextAware, InitializingBean, DisposableBean {

	protected final Logger logger = LoggerFactory.getLogger(this.getClass());
	private final Class<T> beanDefinitionType;
	protected ConfigurableEnvironment environment;
	protected BeanFactory beanFactory;
	private NacosConfigLoader nacosConfigLoader;
	private Properties globalNacosProperties;
	private ClassLoader classLoader;

	private ApplicationEventPublisher applicationEventPublisher;

	public AbstractNacosPropertySourceBuilder() {
		beanDefinitionType = resolveGenericType(getClass());
	}

	/**
	 * Build {@link NacosPropertySource} from {@link BeanDefinition}
	 *
	 * @param beanName Bean name
	 * @param beanDefinition {@link BeanDefinition}
	 * @return a {@link NacosPropertySource} instance
	 */
	public List<NacosPropertySource> build(String beanName, T beanDefinition) {

		Map<String, Object>[] attributesArray = resolveRuntimeAttributesArray(
				beanDefinition, globalNacosProperties);

		int size = attributesArray == null ? 0 : attributesArray.length;

		if (size == 0) {
			return Collections.emptyList();
		}

		List<NacosPropertySource> nacosPropertySources = new ArrayList<NacosPropertySource>(
				size);

		for (int i = 0; i < size; i++) {
			Map<String, Object> attributes = attributesArray[i];
			if (!CollectionUtils.isEmpty(attributes)) {

				NacosPropertySource nacosPropertySource = doBuild(beanName,
						beanDefinition, attributesArray[i]);

				NacosConfigMetadataEvent metadataEvent = createMetaEvent(
						nacosPropertySource, beanDefinition);

				initMetadataEvent(nacosPropertySource, beanDefinition, metadataEvent);

				publishMetadataEvent(metadataEvent);

				nacosPropertySources.add(nacosPropertySource);

			}
		}

		return nacosPropertySources;
	}

	protected abstract NacosConfigMetadataEvent createMetaEvent(
			NacosPropertySource nacosPropertySource, T beanDefinition);

	private void initMetadataEvent(NacosPropertySource nacosPropertySource,
			T beanDefinition, NacosConfigMetadataEvent metadataEvent) {
		metadataEvent.setDataId(nacosPropertySource.getDataId());
		metadataEvent.setGroupId(nacosPropertySource.getGroupId());
		metadataEvent.setBeanName(nacosPropertySource.getBeanName());
		metadataEvent.setBeanType(nacosPropertySource.getBeanType());
		metadataEvent.setNacosProperties(nacosPropertySource.getProperties());
		Map<String, Object> attributesMetadata = nacosPropertySource
				.getAttributesMetadata();
		Map<String, Object> nacosPropertiesAttributes = (Map<String, Object>) attributesMetadata
				.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.PROPERTIES_ATTRIBUTE_NAME);
		metadataEvent.setNacosPropertiesAttributes(nacosPropertiesAttributes);
		doInitMetadataEvent(nacosPropertySource, beanDefinition, metadataEvent);
	}

	private void publishMetadataEvent(NacosConfigMetadataEvent metadataEvent) {
		applicationEventPublisher.publishEvent(metadataEvent);
	}

	protected abstract void doInitMetadataEvent(NacosPropertySource nacosPropertySource,
			T beanDefinition, NacosConfigMetadataEvent metadataEvent);

	protected NacosPropertySource doBuild(String beanName, T beanDefinition,
			Map<String, Object> runtimeAttributes) {

		// Get annotation metadata
		String name = (String) runtimeAttributes.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.NAME_ATTRIBUTE_NAME);
		String dataId = (String) runtimeAttributes.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.DATA_ID_ATTRIBUTE_NAME);
		String groupId = (String) runtimeAttributes.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.GROUP_ID_ATTRIBUTE_NAME);

		dataId = NacosUtils.readFromEnvironment(dataId, environment);
		groupId = NacosUtils.readFromEnvironment(groupId, environment);

		final String type;

		ConfigType typeEunm = (ConfigType) runtimeAttributes.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.CONFIG_TYPE_ATTRIBUTE_NAME);
		if (ConfigType.UNSET.equals(typeEunm)) {
			type = NacosUtils.readFileExtension(dataId);
		}
		else {
			type = typeEunm.getType();
		}

		Map<String, Object> nacosPropertiesAttributes = (Map<String, Object>) runtimeAttributes
				.get(io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource.PROPERTIES_ATTRIBUTE_NAME);

		Properties nacosProperties = resolveProperties(nacosPropertiesAttributes,
				environment, globalNacosProperties);

		String nacosConfig = nacosConfigLoader.load(dataId, groupId, nacosProperties);

		if (!StringUtils.hasText(nacosConfig)) {
			if (logger.isWarnEnabled()) {
				logger.warn(format(
						"There is no content for NacosPropertySource from dataId[%s] , groupId[%s] , properties[%s].",
						dataId, groupId, nacosPropertiesAttributes));
			}
		}

		if (!StringUtils.hasText(name)) {
			name = buildDefaultPropertySourceName(dataId, groupId, nacosProperties);
		}

		NacosPropertySource nacosPropertySource = new NacosPropertySource(dataId, groupId,
				name, nacosConfig, type);

		nacosPropertySource.setBeanName(beanName);

		String beanClassName = beanDefinition.getBeanClassName();
		if (StringUtils.hasText(beanClassName)) {
			nacosPropertySource.setBeanType(resolveClassName(beanClassName, classLoader));
		}
		nacosPropertySource.setGroupId(groupId);
		nacosPropertySource.setDataId(dataId);
		nacosPropertySource.setProperties(nacosProperties);

		initNacosPropertySource(nacosPropertySource, beanDefinition, runtimeAttributes);

		return nacosPropertySource;

	}

	/**
	 * Runtime attributes must contain those:
	 * <ul>
	 * <li>{@link io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource#NAME_ATTRIBUTE_NAME}</li>
	 * <li>{@link  io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource#DATA_ID_ATTRIBUTE_NAME}</li>
	 * <li>{@link  io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource#GROUP_ID_ATTRIBUTE_NAME}</li>
	 * <li>{@link  io.github.chensheng.dddboot.nacos.spring.context.annotation.config.NacosPropertySource#PROPERTIES_ATTRIBUTE_NAME}</li>
	 * </ul>
	 *
	 * @param beanDefinition Bean Definition
	 * @param globalNacosProperties Global Nacos {@link Properties}
	 * @return a non-null attributes array
	 */
	protected abstract Map<String, Object>[] resolveRuntimeAttributesArray(
			T beanDefinition, Properties globalNacosProperties);

	protected abstract void initNacosPropertySource(
			NacosPropertySource nacosPropertySource, T beanDefinition,
			Map<String, Object> attributes);

	/**
	 * Whether target {@link BeanDefinition} supports or not
	 *
	 * @param beanDefinition {@link BeanDefinition}
	 * @return If supports, return <code>true</code>, or <code>false</code>
	 */
	public boolean supports(BeanDefinition beanDefinition) {
		Class<?> beanDefinitionClass = beanDefinition.getClass();
		return beanDefinitionType.isAssignableFrom(beanDefinitionClass);
	}

	@Override
	public void setEnvironment(Environment environment) {
		if (environment instanceof ConfigurableEnvironment) {
			this.environment = (ConfigurableEnvironment) environment;
		}
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}

	@Override
	public void setBeanClassLoader(ClassLoader classLoader) {
		this.classLoader = classLoader;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) {
		ConfigurableApplicationContext context = (ConfigurableApplicationContext) applicationContext;
		this.applicationEventPublisher = new DeferredApplicationEventPublisher(context);
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		nacosConfigLoader = new NacosConfigLoader(environment);
		nacosConfigLoader.setNacosServiceFactory(NacosBeanUtils.getNacosServiceFactoryBean(beanFactory));
		globalNacosProperties = GlobalNacosPropertiesSource.CONFIG.getMergedGlobalProperties(beanFactory);
	}

	@Override
	public void destroy() throws Exception {
		if (nacosConfigLoader == null) {
			return;
		}
		ConfigService configService = nacosConfigLoader.getConfigService();
		if (configService != null) {
			configService.shutDown();
		}
	}

	/**
	 * The type of {@link T Bean Definition}
	 *
	 * @return type of {@link T Bean Definition}
	 */
	public final Class<T> getBeanDefinitionType() {
		return beanDefinitionType;
	}
}
