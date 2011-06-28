/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011 Red Hat Inc. and/or its affiliates and other contributors
 * as indicated by the @authors tag. All rights reserved.
 * See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,  
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.arquillian.container.test.impl.client.container;

import java.util.ArrayList;
import java.util.List;

import org.jboss.arquillian.container.impl.client.container.ContainerLifecycleController;
import org.jboss.arquillian.container.spi.Container;
import org.jboss.arquillian.container.spi.ContainerRegistry;
import org.jboss.arquillian.container.spi.client.deployment.TargetDescription;
import org.jboss.arquillian.container.spi.event.KillContainer;
import org.jboss.arquillian.container.spi.event.SetupContainers;
import org.jboss.arquillian.container.spi.event.StartContainer;
import org.jboss.arquillian.container.spi.event.StopContainer;
import org.jboss.arquillian.container.test.api.ContainerController;
import org.jboss.arquillian.container.test.test.AbstractContainerTestTestBase;
import org.jboss.arquillian.core.api.Instance;
import org.jboss.arquillian.core.api.annotation.ApplicationScoped;
import org.jboss.arquillian.core.api.annotation.Inject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;


/**
 * ClientContainerControllerTestCase
 *
 * @author <a href="mailto:mgencur@redhat.com">Martin Gencur</a>
 * @version $Revision: $
 */
@RunWith(MockitoJUnitRunner.class)
public class ClientContainerControllerTestCase extends AbstractContainerTestTestBase
{
   private static final String DEFAULT_SERVER_NAME = "default";
   private static final String UNKNOWN_SERVER = "unknown";
   
   @Override
   protected void addExtensions(List<Class<?>> extensions)
   {
      extensions.add(ClientContainerControllerCreator.class);
   }
   
   @Inject
   private Instance<ContainerController> controller;
   
   @Before
   public void createSetup()
   {
      ContainerRegistry reg = Mockito.mock(ContainerRegistry.class);
      Container container = Mockito.mock(Container.class);
      Mockito.when(container.getName()).thenReturn(DEFAULT_SERVER_NAME);
      
      List<Container> containers = new ArrayList<Container>();
      containers.add(container);
      Mockito.when(reg.getContainers()).thenReturn(containers);
      Mockito.when(reg.getContainer(new TargetDescription(DEFAULT_SERVER_NAME))).thenReturn(container);
      
      bind(ApplicationScoped.class, ContainerRegistry.class, reg);

      fire(new SetupContainers());
   }

   @Test
   public void shouldFireStartContainerEventOnStart() throws Exception
   {
      controller.get().start(DEFAULT_SERVER_NAME);

      assertEventFired(StartContainer.class, 1);
   }

   @Test
   public void shouldFireStopContainerEventOnStop() throws Exception
   {
      controller.get().stop(DEFAULT_SERVER_NAME);
      
      assertEventFired(StopContainer.class, 1);
   }
   
   @Test
   public void shouldFireKillContainerEventOnKill() throws Exception
   {
      controller.get().kill(DEFAULT_SERVER_NAME);
      
      assertEventFired(KillContainer.class, 1);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionOnStartWhenNotFound() throws Exception
   {
      controller.get().start(UNKNOWN_SERVER);
   }

   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionOnStopWhenNotFound() throws Exception
   {
      controller.get().stop(UNKNOWN_SERVER);
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void shouldThrowExceptionOnKillWhenNotFound() throws Exception
   {
      controller.get().kill(UNKNOWN_SERVER);
   }
}
