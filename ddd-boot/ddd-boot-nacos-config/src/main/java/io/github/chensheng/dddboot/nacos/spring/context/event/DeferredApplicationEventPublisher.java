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
package io.github.chensheng.dddboot.nacos.spring.context.event;

import org.springframework.context.*;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.support.AbstractApplicationContext;

import java.util.Iterator;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Deferred {@link ApplicationEventPublisher} to resolve
 * {@link #publishEvent(ApplicationEvent)} too early to publish {@link ApplicationEvent}
 * when {@link AbstractApplicationContext#initApplicationEventMulticaster() Spring
 * ApplicationContexts' ApplicationEventMulticaster} is not ready, thus current class will
 * hold all early {@link ApplicationEvent events} temporary until
 * {@link ConfigurableApplicationContext#isRunning() Spring ApplicationContext is active},
 * and then those {@link ApplicationEvent events} will be replayed.
 * @author <a href="mailto:mercyblitz@gmail.com">Mercy</a>
 * @since 0.1.0
 */
public class DeferredApplicationEventPublisher
		implements ApplicationEventPublisher, ApplicationListener<ContextRefreshedEvent> {

	private final ConfigurableApplicationContext context;

	// fix issue #85
	private final ConcurrentLinkedQueue<ApplicationEvent> deferredEvents = new ConcurrentLinkedQueue<ApplicationEvent>();

	public DeferredApplicationEventPublisher(ConfigurableApplicationContext context) {
		this.context = context;
		this.context.addApplicationListener(this);
	}

	@Override
	public void publishEvent(ApplicationEvent event) {

		try {
			if (context.isRunning()) {
				context.publishEvent(event);
			}
			else {
				deferredEvents.add(event);
			}
		}
		catch (Exception ignore) {
			deferredEvents.add(event);
		}
	}

	public void publishEvent(Object event) {
		// TODO
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		ApplicationContext currentContext = event.getApplicationContext();

		if (!currentContext.equals(context)) {
			// prevent multiple event multi-casts in hierarchical contexts
			return;
		}

		replayDeferredEvents();
	}

	private void replayDeferredEvents() {
		Iterator<ApplicationEvent> iterator = deferredEvents.iterator();
		while (iterator.hasNext()) {
			ApplicationEvent event = iterator.next();
			publishEvent(event);
			iterator.remove(); // remove if published
		}
	}
}
