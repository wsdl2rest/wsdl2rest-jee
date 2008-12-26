package org.slosc.rest.core;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MultivaluedMap;
import java.util.Set;
import java.util.HashSet;
import java.lang.annotation.Annotation;

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
 *         Date    : Dec 24, 2008
 * @version: 1.0
 */
public class ResourceLocator {

    public Object find(ApplicationContext ctx){
       IdentifyRootResourceClass(ctx);
        
        return null;
    }

    //Identify the root resource class for the request
    private Object IdentifyRootResourceClass(ApplicationContext ctx){

        //(a) Set U = request URI path;C = {root resource classes};E = {}
        Request req = (Request)ctx.get(Request.class.getName());
        String u = req.getPath();
        Set<Class<?>> c = ctx.getClasses();
        Set<String> e = new HashSet<String>();
        r(c, e);

        /*
        (c) Filter E by matching each member against U as follows:
            - Remove members that do not match U.
            - Remove members for which the final regular expression capturing group (henceforth simply
            referred to as a capturing group) value is neither empty nor ‘/’ and the class associated with 
            R(Tclass) had no sub-resource methods or locators.
         */
        for(String path:e){
            if(!path.equals(u)) e.remove(path);
        }

        if(e.size() == 0) {
            throw new WebApplicationException();
        }
        
        
        return null;
    }

    /**
     * (b) For each class in C add a regular expression (computed using the function R(A) described below)
     * to E as follows:  Add R(Tclass) where Tclass is the URI path template specified for the class.
     *
     * Converting URI Templates to Regular Expressions
     * The function R(A) converts a URI path template annotation A into a regular expression as follows:
     *   1. URI encode the template, ignoring URI template variable specifications.
     *   2. Escape any regular expression characters in the URI template, again ignoring URI template variable
     *   specifications. 
     *   3. Replace each URI template variable with a capturing group containing the specified regular expression
     *   or ‘([ˆ/]+?)’ if no regular expression is specified.
     *   4. If the resulting string ends with ‘/’ then remove the final character.
     *   5. Append ‘(/.*)?’ to the result.
     * @return
     */
    private void r(Set<Class<?>> c, Set e){
        for(Class<?> cc: c){
            Path path = getPathAnnoation(cc);

            //class is path anotated
            if(path != null){
                String p = path.value();
                MultivaluedMapImpl<String, Class> entry = new MultivaluedMapImpl<String, Class>();
                entry.putSingle(p, cc);
                e.add(entry);
            }
        }

    }


    /**
     * A root resource class is anchored in URI space using the @Path annotation. The value of the annotation is
     * a relative URI path template whose base URI is provided by the deployment context.
     * A URI path template is a string with zero or more embedded parameters that, when values are substituted
     * for all the parameters, is a valid URI[5] path. The Javadoc for the @Path annotation describes their syntax
     * {@link javax.ws.rs.Path}
     */
    private void uriTemplate(){

    }

    private Path getPathAnnoation(Class<?> c){
        Path path = c.getAnnotation(Path.class);
        if(path == null){
            for (Class<?> i : c.getInterfaces())
            if (i.isAnnotationPresent(Path.class)) {
                path = i.getAnnotation(Path.class); break;
            }
        }
        return path;
    }


}
