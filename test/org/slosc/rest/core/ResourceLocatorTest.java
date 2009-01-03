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

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 * Date    : Jan 1, 2009
 * @version: 1.0
 */

import junit.framework.*;
import org.slosc.rest.core.ResourceLocator;
import org.slosc.rest.core.resource.ResourceClassLoader;
import org.slosc.rest.core.resource.ResourceClassParser;
import org.slosc.rest.servlet.RequestWrapper;
import org.slosc.rest.servlet.ResponseWrapper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.net.URL;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResourceLocatorTest extends TestCase {
    ResourceLocator resourceLocator;
    ResourceClassLoader resourceClassLoader;
    ApplicationConfiguration applicationConfig;

    protected void setUp() throws Exception {
        resourceLocator = new ResourceLocator();
        resourceClassLoader = new ResourceClassLoader();
        resourceClassLoader.setParser(new ResourceClassParser());

        URL url = this.getClass().getClassLoader().getResource("org/slosc/wsdl2rest/samples/Bookstore.class");
        resourceClassLoader.addLookupPath(url.getPath());
        url = this.getClass().getClassLoader().getResource("org/slosc/wsdl2rest/samples/BookstoreWithPattern.class");
        resourceClassLoader.addLookupPath(url.getPath());
        resourceClassLoader.lookup();
        Set<Class<?>> cls = resourceClassLoader.getResourceClasses();
        for(Class cc: cls){
            System.out.println(cc.getName());
        }

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) classLoader = getClass().getClassLoader();
        applicationConfig = (ApplicationConfiguration) classLoader.loadClass("org.slosc.rest.core.ApplicationConfiguration").newInstance();

    }

    public void testLookup() throws Exception {
        try {

//         Matcher m = Pattern.compile("/bookstore/").matcher("/bookstore/item/101");
//         boolean matches = m.find();

            Request<HttpServletRequest> reqest    = new MockRequestWrapper("/bookstore/item/101");
            Response<HttpServletResponse> response = new MockResponseWrapper(null);

            //create per request application context from the application configuration.
            ApplicationContext ctx = new ApplicationContext(applicationConfig);

            ctx.add(Request.class.getName(), reqest);
            ctx.add(Response.class.getName(), response);
            ctx.setResourceClassLoader(resourceClassLoader);

            Object o = resourceLocator.find(ctx);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void testLookup2() throws Exception {
        try {
            Request<HttpServletRequest> reqest    = new MockRequestWrapper("/bookstore/colombo/item/101");
            Response<HttpServletResponse> response = new MockResponseWrapper(null);

            //create per request application context from the application configuration.
            ApplicationContext ctx = new ApplicationContext(applicationConfig);

            ctx.add(Request.class.getName(), reqest);
            ctx.add(Response.class.getName(), response);
            ctx.setResourceClassLoader(resourceClassLoader);

            Object o = resourceLocator.find(ctx);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    class MockRequestWrapper extends RequestWrapper{
        private String path;

        public MockRequestWrapper(String path) {
            super(null);
            this.path = path;
        }

        public String getPath() {
            return path;
        }

        public String getMethod() {
            return "GET";    
        }
    }

    class MockResponseWrapper extends ResponseWrapper{

        public MockResponseWrapper(HttpServletResponse res) {
            super(res);
        }
    }
}