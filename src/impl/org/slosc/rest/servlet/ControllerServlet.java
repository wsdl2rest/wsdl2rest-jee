package org.slosc.rest.servlet;

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

import org.slosc.rest.core.ApplicationConfiguration;
import org.slosc.rest.core.RequestHandler;
import org.slosc.rest.core.ApplicationContext;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.ws.rs.core.UriBuilder;
import java.io.IOException;

/**
 * JAX-RS implementation Servlet class to be used in non-JAX-RS aware servlet container
 * <servlet-class/> of web.xml shoud define this servlet.  if the servlet container is
 * JAX-RS aware, the <servlet-class/> element of the web.xml descriptor SHOULD name
 * the application-supplied subclass of <code>Application</code>
 *
 * The application-supplied subclass of <code>Application</code> is identified using
 * an init-param with a param-name of javax.ws.rs.Application
 *
 * By default a new resource class instance is created for each request to that resource.
 * i.e. request scope. inversion-of-control framework may support all of the other lifecycle options provided by
 * that framework. First the constructor (see section 3.1.2) is called, then any requested dependencies are
 * injected (see section 3.2), then the appropriate method (see section 3.3) is invoked and
 * finally the object is made available for garbage collection
 *
 * A public constructor MAY include parameters annotated with one of the following:
 * @Context, @Header, @Param, @CookieParam, @MatrixParam, @QueryParam or @PathParam.
 * However, depending on the resource class lifecycle and concurrency, per-request information may not
 * make sense in a constructor. If more than one public constructor is suitable then an implementation MUST
 * use the one with the most parameters. Choosing amongst suitable constructors with the same number of
 * parameters is implementation specific, implementations SHOULD generate a warning about such ambiguity.
 *
 * Non-root resource classes are instantiated by an application and do not require the above-described
 * public constructor
 *
 * When a resource class is instantiated
 */

public class ControllerServlet extends HttpServlet {

    private final static String  APP_CONFIG_CLASS_NAME = "javax.ws.rs.Application";
    private ApplicationConfiguration applicationConfig;
    private ServletConfig config = null;

    private ApplicationContext ctx;

    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        config = servletConfig;
        String applicationConfigClassName = getInitParameter(APP_CONFIG_CLASS_NAME);

        //use the default is no config is found
        if(applicationConfigClassName == null) applicationConfigClassName = "org.slosc.rest.core.ApplicationConfiguration";

        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        if(classLoader == null) classLoader = getClass().getClassLoader();

        try {
            applicationConfig = (ApplicationConfiguration) classLoader.loadClass(applicationConfigClassName).newInstance();
        } catch (ClassNotFoundException e) {
            log("Unable to load Application configuration class ", e);  
        } catch (IllegalAccessException e) {
            log("Unable to load Application configuration class ", e);
        } catch (InstantiationException e) {
            log("Unable to load Application configuration class ", e); 
        }

        ctx = new ApplicationContext();
        ctx.add("applicationConfig", applicationConfig);
    }
    
    protected void service(HttpServletRequest req, HttpServletResponse res) 
            throws ServletException, IOException {
//        super.service(req, res);

        if (req.getPathInfo() != null && req.getPathInfo().equals("/") && !req.getRequestURI().endsWith("/")) {
            res.setStatus(404);
            return;
        }
        
        UriBuilder absURI = UriBuilder.fromUri(req.getRequestURL().toString());
        
        String queryParameters = req.getQueryString();
        if (queryParameters == null) queryParameters = "";

        RequestHandler handler = new RequestHandler();

        try {
            handler.invoke(ctx);
        } catch (Exception e) {
            res.sendError(500, e.getMessage());
            return;
        }
    }
}
