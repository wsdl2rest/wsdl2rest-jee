package org.slosc.rest.core.resource;
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

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 * Date    : Dec 29, 2008
 * @version: 1.0
 */

import junit.framework.*;
import org.slosc.rest.core.resource.ResourceClassLoader;

import java.net.URL;
import java.util.Set;

public class ResourceClassLoaderTest extends TestCase {
    ResourceClassLoader resourceClassLoader;

    protected void setUp() throws Exception {
        resourceClassLoader = new ResourceClassLoader();
        resourceClassLoader.setParser(new ResourceClassParser());
    }

    public void testLookup() throws Exception {
        try {
            URL url = this.getClass().getClassLoader().getResource("org/slosc/wsdl2rest/samples/Bookstore.class");
//            String file = url.getPath().substring(0, url.getPath().length() - "Bookstore.class".length()); //need a path not a file
            resourceClassLoader.addLookupPath(url.getPath());
            resourceClassLoader.lookup();
            Set<Class<?>> cls = resourceClassLoader.getResourceClasses();
            for(Class cc: cls){
                System.out.println(cc.getName());
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}