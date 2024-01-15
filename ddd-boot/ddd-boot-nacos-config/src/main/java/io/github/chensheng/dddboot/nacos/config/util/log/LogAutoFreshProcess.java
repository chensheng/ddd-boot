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

package io.github.chensheng.dddboot.nacos.config.util.log;

import com.alibaba.nacos.api.common.Constants;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.AbstractListener;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.client.config.utils.ConcurrentDiskUtil;
import com.alibaba.nacos.client.config.utils.JvmUtil;
import com.alibaba.nacos.client.logging.NacosLogging;
import com.alibaba.nacos.client.utils.LogUtils;
import com.alibaba.nacos.common.utils.IoUtils;
import com.alibaba.nacos.common.utils.StringUtils;
import io.github.chensheng.dddboot.nacos.config.properties.NacosConfigProperties;
import io.github.chensheng.dddboot.nacos.config.util.NacosConfigLoader;
import io.github.chensheng.dddboot.nacos.spring.util.NacosUtils;
import org.slf4j.Logger;
import org.springframework.boot.context.logging.LoggingApplicationListener;
import org.springframework.boot.logging.LoggingInitializationContext;
import org.springframework.boot.logging.LoggingSystem;
import org.springframework.boot.logging.LoggingSystemFactory;
import org.springframework.core.env.ConfigurableEnvironment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;

/**
 * Start:
 * Step1: get the log XML configuration from the configuration center
 * Step2: modify the springboot log configuration path
 * Modifying log configuration during operation:
 * Clean up the configuration through LoggingSystem and reload the configuration.
 *
 * @author <a href="mailto:hujun3@xiaomi.com">hujun</a>
 */
public class LogAutoFreshProcess {

    private static final Logger LOGGER = LogUtils.logger(LogAutoFreshProcess.class);

    private final NacosConfigProperties nacosConfigProperties;

    private final ConfigurableEnvironment environment;

    private final NacosConfigLoader nacosConfigLoader;

    private final Function<Properties, ConfigService> builder;

    private static final List<String> LOG_DATA_ID = new ArrayList<>();

    private static final String LOG_CACHE_BASE = System.getProperty("JM.SNAPSHOT.PATH", System.getProperty("user.home")) + File.separator + "nacos"
            + File.separator + "logConfig";

    static {
        LOG_DATA_ID.add("logback.xml");
        LOG_DATA_ID.add("log4j2.xml");
    }

    public static LogAutoFreshProcess build(ConfigurableEnvironment environment, NacosConfigProperties nacosConfigProperties, NacosConfigLoader nacosConfigLoader, Function<Properties, ConfigService> builder) {
        return new LogAutoFreshProcess(environment, nacosConfigProperties, nacosConfigLoader, builder);
    }

    private LogAutoFreshProcess(ConfigurableEnvironment environment, NacosConfigProperties nacosConfigProperties, NacosConfigLoader nacosConfigLoader, Function<Properties, ConfigService> builder) {
        this.nacosConfigProperties = nacosConfigProperties;
        this.environment = environment;
        this.nacosConfigLoader = nacosConfigLoader;
        this.builder = builder;
    }

    public void process() {
        final String groupName = environment
                .resolvePlaceholders(nacosConfigProperties.getGroup());
        ConfigService configService = builder.apply(nacosConfigLoader.getGlobalProperties());
        for (String dataId : LOG_DATA_ID) {
            String content = NacosUtils.getContent(configService, dataId, groupName);
            if (StringUtils.isNotBlank(content)) {
                writeLogFile(content, dataId);
                System.setProperty(LoggingApplicationListener.CONFIG_PROPERTY, LOG_CACHE_BASE + File.separator + dataId);
                registerListener(configService, dataId, groupName);
                return;
            }
        }
    }

    private void registerListener(ConfigService configService, String dataId, String groupName) {
        try {
            configService.addListener(dataId, groupName, new AbstractListener() {
                @Override
                public void receiveConfigInfo(String configInfo) {
                    if (StringUtils.isNotBlank(configInfo)) {
                        writeLogFile(configInfo, dataId);
                        reloadConfig(LOG_CACHE_BASE + File.separator + dataId);
                    }
                }
            });
        } catch (NacosException e) {
            throw new RuntimeException("ConfigService can't add Listener with dataId : " + dataId, e);
        }
    }

    private void writeLogFile(String content, String dataId) {
        File file = new File(LOG_CACHE_BASE, dataId);
        File parentFile = file.getParentFile();
        if (!parentFile.exists()) {
            boolean isMdOk = parentFile.mkdirs();
            if (!isMdOk) {
                LOGGER.error("save log cache error");
            }
        }
        try {
            if (JvmUtil.isMultiInstance()) {
                ConcurrentDiskUtil.writeFileContent(file, content, Constants.ENCODE);
            } else {
                IoUtils.writeStringToFile(file, content, Constants.ENCODE);
            }
        } catch (IOException e) {
            throw new RuntimeException("write log file fail");
        }
    }

    private void reloadConfig(String logPath) {
        LoggingSystem loggingSystem = LoggingSystemFactory.fromSpringFactories()
                .getLoggingSystem(this.getClass().getClassLoader());
        loggingSystem.cleanUp();
        loggingSystem.initialize(new LoggingInitializationContext(environment),
                logPath, null);
        NacosLogging.getInstance().loadConfiguration();
    }

}
