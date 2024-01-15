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
package io.github.chensheng.dddboot.nacos.spring.context.event.config;

import com.alibaba.nacos.api.config.ConfigService;
import org.springframework.context.ApplicationEvent;

/**
 * The Event of Nacos Configuration is used on Spring Event
 *
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 0.1.0
 */
public abstract class NacosConfigEvent extends ApplicationEvent {

	private final String dataId;

	private final String groupId;

	/**
	 * @param configService Nacos {@link ConfigService}
	 * @param dataId data ID
	 * @param groupId group ID
	 */
	public NacosConfigEvent(ConfigService configService, String dataId, String groupId) {
		super(configService);
		this.dataId = dataId;
		this.groupId = groupId;
	}

	@Override
	public final ConfigService getSource() {
		return (ConfigService) super.getSource();
	}

	/**
	 * Get {@link ConfigService}
	 *
	 * @return {@link ConfigService}
	 */
	public final ConfigService getConfigService() {
		return getSource();
	}

	public final String getDataId() {
		return dataId;
	}

	public final String getGroupId() {
		return groupId;
	}
}
