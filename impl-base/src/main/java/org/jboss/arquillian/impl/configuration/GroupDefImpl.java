/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat Middleware LLC, and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
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
package org.jboss.arquillian.impl.configuration;

import org.jboss.arquillian.impl.configuration.api.ContainerDef;
import org.jboss.arquillian.impl.configuration.api.GroupDef;
import org.jboss.arquillian.impl.configuration.model.ArquillianModel;
import org.jboss.arquillian.impl.configuration.model.ContainerImpl;
import org.jboss.arquillian.impl.configuration.model.GroupImpl;

/**
 * GroupDefImpl
 *
 * @author <a href="mailto:aslak@redhat.com">Aslak Knutsen</a>
 * @version $Revision: $
 */
public class GroupDefImpl extends ArquillianDescriptorImpl implements GroupDef
{
   private GroupImpl group;
   
   public GroupDefImpl(ArquillianModel model, GroupImpl group)
   {
      super(model);
      this.group = group;
   }
   
   //-------------------------------------------------------------------------------------||
   // Required Implementations - ProtocolDescriptor --------------------------------------||
   //-------------------------------------------------------------------------------------||

   /* (non-Javadoc)
    * @see org.jboss.arquillian.impl.configuration.ArquillianDescriptorImpl#container(java.lang.String)
    */
   @Override
   public ContainerDef container(String name)
   {
      ContainerImpl container = new ContainerImpl(name);
      group.getContainers().add(container);
      return new GroupContainerDefImpl(getSchemaModel(), group, container);
   }

}