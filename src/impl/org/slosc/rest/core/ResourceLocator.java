package org.slosc.rest.core;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.HttpMethod;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;
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

    private MultivaluedMapImpl<String, UriTemplateRegEx> classEntries = new MultivaluedMapImpl<String, UriTemplateRegEx>();
    private MultivaluedMapImpl<String, UriTemplateRegEx> methodEntries = new MultivaluedMapImpl<String, UriTemplateRegEx>();

    class UriTemplateComparator implements Comparator<String>{
        private MultivaluedMapImpl<String, UriTemplateRegEx> classEntries;
        
        UriTemplateComparator(MultivaluedMapImpl<String, UriTemplateRegEx> classEntries) {
            this.classEntries = classEntries;
        }

        public int compare(String left, String right) {
            UriTemplateRegEx leftRegex = classEntries.get(left).get(0);
            UriTemplateRegEx rightRegex = classEntries.get(right).get(0);
            int leftdiff = leftRegex.getGcount() -  leftRegex.getDefaultRegexLocIndx();
            int rightdiff = rightRegex.getGcount() -  rightRegex.getDefaultRegexLocIndx();

            //TODO fix me number of literal characters
            int primaryKey = right.compareTo(left);
            int secondery = leftRegex.getGcount() > rightRegex.getGcount()? 1:leftRegex.getGcount() == rightRegex.getGcount()?0:-1;
            int tertiary = leftdiff > rightdiff?1:leftdiff == rightdiff?0:-1;
            //TODO fix me
            return primaryKey;//+secondery+tertiary;
        }
    }

    public Object find(ApplicationContext ctx){
        return IdentifyRootResourceClass(ctx);
    }

    //Identify the root resource class for the request
    private Object IdentifyRootResourceClass(ApplicationContext ctx){

        //(a) Set U = request URI path;C = {root resource classes};E = {}
        Request req = (Request)ctx.get(Request.class.getName());
        String u = req.getPath();
        Set<Class<?>> c = ctx.getClasses();
        Set<String> e = new HashSet<String>();
        resolve(c, e);

        /*
        (c) Filter E by matching each member against U as follows:
            - Remove members that do not match U.
            - Remove members for which the final regular expression capturing group (henceforth simply
              referred to as a capturing group) value is neither empty nor ‘/’ and the class associated with
              R(Tclass) had no sub-resource methods or locators.
         */
        Set<String> toRemove = new HashSet<String>();
        for(String path: e){
            if(!match(u, path, classEntries)) {
                toRemove.add(path);
            }
            UriTemplateRegEx regex = classEntries.get(path).get(0);
            String fcapgrp = regex.getFinalCapturingGroup();
            if(fcapgrp != null && fcapgrp.equals("/")){
                toRemove.add(path);
            }
        }
        e.removeAll(toRemove);

        if(e.size() == 0) {
            System.err.println("ERROR: No resource found ... ");
            throw new WebApplicationException(404);
        }

        if(e.size() > 1) {
            System.err.println("WARN: There exist more than one matching resource for the uri: "+u);
        }

        /*
         * (e) Sort E using the number of literal characters in each member as the primary key (descending
         *   order), the number of capturing groups as a secondary key (descending order) and the number
         *   of capturing groups with non-default regular expressions (i.e. not ‘([ˆ/]+?)’) as the tertiary key
         *   (descending order).
         */
        UriTemplateComparator cmp = new UriTemplateComparator(classEntries);
        Arrays.sort(e.toArray(), (Comparator)cmp);

       /* (f) Set Rmatch to be the first member of E, set U to be the value of the final capturing group of
        *  Rmatch when matched against U, and instantiate an object O of the associated class.
        */
        Iterator itr = e.iterator();
        List<UriTemplateRegEx> reg = classEntries.get(itr.next());
        UriTemplateRegEx regToInvoke= reg.get(0);

         //ok we have the required class, and variables as of now.
        Class resource =  regToInvoke.getForClazz();

        //TODO verify
        u = regToInvoke.getFinalCapturingGroup();

        /*
         *   Methods of a resource class that are annotated with @Path are either sub-resource methods or sub-resource
         *   locators. Sub-resource methods handle a HTTP request directly whilst sub-resource locators return an object
         *   that will handle a HTTP request. The presence or absence of a request method designator (e.g. @GET)
         *   differentiates between the two:
         *
         *   Present: Such methods, known as sub-resource methods, are treated like a normal resource method
         *   except the method is only invoked for request URIs that match a URI template created by
         *   concatenating the URI template of the resource class with the URI template of the method.
         *   (If the resource class URI template does not end with a ‘/’ character then one is added during the concatenation.)
         *
         *   TODO Absent: Such methods, known as sub-resource locators, are used to dynamically resolve the object that will handle the request.
         */

        //check http method support
        Method[] methods = resource.getMethods();
        Set<Method> subResMethods = new HashSet<Method>();
        Set<Method> otherMethods = new HashSet<Method>();
        Set<Method> m = new HashSet<Method>();

        for(Method mth:methods){
            if(mth.isAnnotationPresent(Path.class)){
                //either sub-resource method/locators
                for(Annotation an :mth.getAnnotations()){
                    if(an.annotationType().isAnnotationPresent(HttpMethod.class)
                      && an.annotationType().getAnnotation(HttpMethod.class).value().equals(req.getMethod())){
                        subResMethods.add(mth); break;
                    }else {//sub-resource locators
                        subResMethods.add(mth); break;
                    }
                }
            }else{
                 for(Annotation an :mth.getAnnotations()){
                    if(an.annotationType().isAnnotationPresent(HttpMethod.class)
                      && an.annotationType().getAnnotation(HttpMethod.class).value().equals(req.getMethod())){
                        otherMethods.add(mth);
                    }
                }
            }
        }

        if(u != null && !u.equals("/")){

            if(subResMethods.size() == 0) {
                System.err.println("ERROR: No resource found with methods supporting the request method... ");
                throw new WebApplicationException(405);
            }

            e = new HashSet<String>();
            resolveSubResourceMethods(subResMethods, e);

            toRemove = new HashSet<String>();
            for(String path: e){
                if(!match(u, path, methodEntries))  {
                    toRemove.add(path);
                }
                UriTemplateRegEx regex = methodEntries.get(path).get(0);
                String fcapgrp = regex.getFinalCapturingGroup();
                if(fcapgrp != null && fcapgrp.equals("/")){
                    toRemove.add(path);
                }
            }
            e.removeAll(toRemove);

            if(e.size() == 0) {
                System.err.println("ERROR: No resource method found ... ");
                throw new WebApplicationException(404);
            }

            if(e.size() > 1) {
                System.err.println("WARN: There exist more than one matching resource for the uri: "+u);
            }

            //TODO the source of each member as quaternary key sorting those derived from Tmethod ahead of those derived from Tlocator.
            cmp = new UriTemplateComparator(methodEntries);
            Arrays.sort(e.toArray(), (Comparator)cmp);

           /* (f) Set Rmatch to be the first member of E, set U to be the value of the final capturing group of
            *  Rmatch when matched against U, and instantiate an object O of the associated class.
            */
            itr = e.iterator();
            reg = methodEntries.get(itr.next());
    //        regToInvoke= reg.get(0);

            for(UriTemplateRegEx r:reg){
                for(Annotation an :r.getForMethod().getAnnotations()){
                    if(an.annotationType().isAnnotationPresent(HttpMethod.class)){
                        m.add(r.getForMethod());
                    }else{
                        //sub-resource locator
                        u = regToInvoke.getFinalCapturingGroup();
                        //invoke sub-resource
                        System.err.println("ERROR: Sub resources are not support as yet ... ");
                        throw new WebApplicationException(405);
                    }
                }
            }
        }else if(u == null || u.equals("/")){
            m.addAll(otherMethods);
        }
        
        if(m.size() == 0) {
            System.err.println("ERROR: No resource found with methods supporting the request method... ");
            throw new WebApplicationException(405);
        }

//        cmp = new UriTemplateComparator(methodEntries);
//        Arrays.sort(m.toArray(), (Comparator)cmp);
        
         //ok we have the required class, and variables as of now.

        itr = m.iterator();
        reg = methodEntries.get(itr.next());
        regToInvoke= reg.get(0);

         //ok we have the required class, and variables as of now.
        Method methodToInvoke =  regToInvoke.getForMethod();

        try {
            methodToInvoke.invoke(resource.newInstance(), new Object[]{"TODO this is a test"});
        } catch (InstantiationException ex) {
            ex.printStackTrace();  //TODO change body of catch statement
            throw new WebApplicationException(ex);
        } catch (IllegalAccessException ex) {
            ex.printStackTrace();  //TODO change body of catch statement
            throw new WebApplicationException(ex);
        } catch (InvocationTargetException e1) {
            e1.printStackTrace();  //TODO change body of catch statement
        }

        return null;
    }

    private boolean match(String uri, String path, MultivaluedMapImpl<String, UriTemplateRegEx> entries){
        List<UriTemplateRegEx> reg = entries.get(path);

        //??
        if(reg.size() == 0 || reg.size() > 1) return false;

        Matcher m = Pattern.compile(path).matcher(uri);
        boolean matches = m.find();
        if(matches){
            int gcount = m.groupCount();
            UriTemplateRegEx uriTemplateRegEx = reg.get(0);
            if(gcount > 0) {
                int vsize = uriTemplateRegEx.getVaribalesIndx().size();
                matches =  vsize == gcount - 1; //deduct one for ‘(/.*)?’ group
                uriTemplateRegEx.setGcount(gcount);
                if(matches){
                    Map<String, String> varibales = new HashMap<String, String>();
                    Map<Integer, String>  vIndexs =  uriTemplateRegEx.getVaribalesIndx();
                    for(int i=0; i < vsize;i++){
                        varibales.put(vIndexs.get(i), m.group(i+1));
                    }
                    uriTemplateRegEx.setVaribales(varibales);
                }
                uriTemplateRegEx.setFinalCapturingGroup(m.group(gcount));
            }
            uriTemplateRegEx.setMainPath(m.group());
        }
        return matches;
    }

    /**
     * (b) For each class in C add a regular expression (computed using the function R(A) described below)
     * to E as follows:  Add R(Tclass) where Tclass is the URI path template specified for the class.
     *
     *  @return
     */
    private void resolve(Set<Class<?>> c, Set e){
        for(Class<?> cc: c){
            Path path = getPathAnnoation(cc);

            //class is path anotated
            if(path != null){
                UriTemplateRegEx reg = r(path);
                reg.setForClazz(cc);
                classEntries.putSingle(reg.getPath(), reg);
                e.add(reg.getPath());
            }
        }

    }

    private void resolveSubResourceMethods(Set<Method> subResMethods, Set<String> e) {
        resolveMethods(subResMethods, e);
    }

    private void resolveMethods(Set<Method> method, Set e){
        for(Method m: method){
            Path path = m.getAnnotation(Path.class);

            //class is path anotated
            if(path != null){
                UriTemplateRegEx reg = r(path);
                reg.setForMethod(m);
                methodEntries.putSingle(reg.getPath(), reg);
                e.add(reg.getPath());
            }
        }
    }


    /**
     * Converting URI Templates to Regular Expressions
     *
     * A root resource class is anchored in URI space using the @Path annotation. The value of the annotation is
     * a relative URI path template whose base URI is provided by the deployment context.
     * A URI path template is a string with zero or more embedded parameters that, when values are substituted
     * for all the parameters, is a valid URI path. The Javadoc for the @Path annotation describes their syntax
     * {@link javax.ws.rs.Path}
     *
     * Template parameters can optionally specify the regular expression used to match their values.
     *
     * The function R(A) converts a URI path template annotation A into a regular expression as follows:
     *   1. URI encode the template, ignoring URI template variable specifications.
     *   2. Escape any regular expression characters in the URI template, again ignoring URI template variable
     *   specifications.
     *   3. Replace each URI template variable with a capturing group containing the specified regular expression
     *   or ‘([ˆ/]+?)’ if no regular expression is specified.
     *   4. If the resulting string ends with ‘/’ then remove the final character.
     *   5. Append ‘(/.*)?’ to the result.
     *
     * @param path
     * @return
     */

    private UriTemplateRegEx r(Path path){
        String uriTemplate = path.value().trim();

//      if(!Character.isLetter(uriTemplate.charAt(0)) && uriTemplate.charAt(0) != '_') throw new IllegalArgumentException("Invalid path");

        //strip the template varible
        int start = uriTemplate.indexOf('{');

        StringBuilder pathStr = new StringBuilder();
        if(start < 0) {
            URI uri = null;
            try {
                uri = new URI(uriTemplate);
            } catch (URISyntaxException e) {
                e.printStackTrace();  //TODO change body of catch statement
            }
            pathStr.append(uri.toASCIIString());
        }

        Map varibalesIndx = new HashMap();
        int groupIndx = 0;
        int [] loc = new int[25];
        int j = 0;

        while(start >= 0){
            URI uri = null;
            try {
                uri = new URI(uriTemplate.substring(0, start));
            } catch (URISyntaxException e) {
                e.printStackTrace();  //TODO change body of catch statement
            }
            uriTemplate = uriTemplate.substring(start+1).trim();
            pathStr.append(escape(uri.toASCIIString()));

            int i=0;
            char [] tmpVariable = new char[uriTemplate.length()];
            while(isNameChar(uriTemplate.charAt(i))){
                tmpVariable[i] = uriTemplate.charAt(i);i++;
            }

            uriTemplate = uriTemplate.substring(i).trim();

            int end = uriTemplate.indexOf('}');
            if (end < 0) throw new IllegalArgumentException("Invalid path variable definition");

            String variable = new String(tmpVariable, 0, i);
            String regex = "";
            //check for patterns
            start = 0;
            if(uriTemplate.charAt(start) == ':'){
               regex = uriTemplate.substring(start+1, end).trim();
            }
            
            uriTemplate = uriTemplate.substring(end+1);

            regex = regex.length() > 0?"("+regex+")":"([ˆ/]+?)";
            if(regex.length() <= 0) loc[j++] = groupIndx;
            varibalesIndx.put(groupIndx++, variable);
            pathStr.append(regex);

            start = uriTemplate.indexOf('{');
        }

        if(pathStr.charAt(0) != '/') pathStr = pathStr.append('/');
        if(pathStr.charAt(pathStr.length() - 1) == '/') pathStr = pathStr.deleteCharAt(pathStr.length() - 1);
        pathStr.append("(/.*)?");

        UriTemplateRegEx ret = new UriTemplateRegEx(pathStr.toString(), varibalesIndx);
        for (int aLoc : loc) {
            ret.addToDefaultRegexLoc(aLoc);
        }

        return ret;
    }

    private static boolean isNameChar(char c){
        return Character.isLetterOrDigit(c) || c == '_' || c == '.' || c == '-';
    }

    private static String escape(String str){
        int size = str.length();
        char [] tmp = new char[size * 2];
        int j=0;
        for (int i=0;i<size;i++){
            char c = str.charAt(i);
            if(c == '.' || c == '(' || c == ')' || c == '?' || c == '+') tmp[j++] = '\\';
            tmp[j++] = c;
        }
        return new String(tmp, 0, j);
    }

    private static Path getPathAnnoation(Class<?> c){
        Path path = c.getAnnotation(Path.class);
        if(path == null){
            for (Class<?> i : c.getInterfaces())
            if (i.isAnnotationPresent(Path.class)) {
                path = i.getAnnotation(Path.class); break;
            }
        }
        return path;
    }

//    private String encode(String str){
//       Charset utf8 = Charset.forName("UTF-8");
//
//
//       for(int i=0;i<str.length();i++){
//            char c = str.charAt(i);
//       }
//       return "";
//    }

}
