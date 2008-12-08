package org.slosc.rest.core;

/*
 * Copyright (c) 2008 SL_OpenSource Consortium
 * All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import org.slosc.rest.core.resource.ResourceClassLoader;

import javax.ws.rs.core.Application;
import java.util.Set;
import java.util.HashSet;
import java.net.URL;

/**
 * A JAX-RS application consists of one or more resources (see chapter 3) and zero or more providers (see
 * chapter 4). This chapter describes aspects of JAX-RS that apply to an application as a whole, subsequent
 * chapters describe particular aspects of a JAX-RS application and requirements on JAX-RS implementations.
 *
 * The resources and providers that make up a JAX-RS application are configured via an application-supplied
 * subclass of Application.
 *
 * An implementation MAY provide alternate mechanisms for locating resource classes and providers (e.g.
 * runtime class scanning) but use of Application is the only portable means of configuration.
 */

public class ApplicationConfiguration extends Application {

    private final Set<Class<?>> classes = new HashSet<Class<?>>();
    private ResourceClassLoader resourceClassLoader = new ResourceClassLoader();

    public ApplicationConfiguration(){
    }

    public Set<Class<?>> getClasses() {
        return classes;  
    }

    private boolean validate(){
        return false;
    }

    public void loadResources() throws Exception {
        resourceClassLoader.lookup();
    }
    public ResourceClassLoader getResourceClassLoader() {
        return resourceClassLoader;
    }

    public void setResourceClassLoader(ResourceClassLoader resourceClassLoader) {
        this.resourceClassLoader = resourceClassLoader;
    }
}
