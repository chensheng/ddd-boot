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
package io.github.chensheng.dddboot.nacos.spring.context.annotation.config;

import io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.env.Environment;
import org.springframework.core.env.PropertyResolver;
import org.springframework.core.type.AnnotationMetadata;

import static io.github.chensheng.dddboot.nacos.spring.util.NacosBeanUtils.*;
import static org.springframework.core.annotation.AnnotationAttributes.fromMap;

/**
 * Nacos Config {@link ImportBeanDefinitionRegistrar BeanDefinition Registrar}
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @see EnableNacosConfig
 * @see NacosBeanUtils#registerGlobalNacosProperties(AnnotationAttributes,
 * BeanDefinitionRegistry, PropertyResolver, String)
 * @see NacosBeanUtils#registerNacosCommonBeans(BeanDefinitionRegistry)
 * @see NacosBeanUtils#registerNacosConfigBeans(BeanDefinitionRegistry,
 * Environment,BeanFactory)
 * @since 0.1.0
 */
public class NacosConfigBeanDefinitionRegistrar
		implements ImportBeanDefinitionRegistrar, EnvironmentAware, BeanFactoryAware {

	private Environment environment;

	private BeanFactory beanFactory;

	@Override
	public void registerBeanDefinitions(AnnotationMetadata metadata,
			BeanDefinitionRegistry registry) {
		AnnotationAttributes attributes = fromMap(
				metadata.getAnnotationAttributes(EnableNacosConfig.class.getName()));

		// Register Global Nacos Properties Bean
		registerGlobalNacosProperties(attributes, registry, environment,
				CONFIG_GLOBAL_NACOS_PROPERTIES_BEAN_NAME);
		// Register Nacos Common Beans
		registerNacosCommonBeans(registry);
		// Register Nacos Config Beans
		registerNacosConfigBeans(registry, environment, beanFactory);
		// Invoke NacosPropertySourcePostProcessor immediately
		// in order to enhance the precedence of @NacosPropertySource process

		invokeNacosPropertySourcePostProcessor(beanFactory);
	}

	@Override
	public void setEnvironment(Environment environment) {
		this.environment = environment;
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
		this.beanFactory = beanFactory;
	}
}
