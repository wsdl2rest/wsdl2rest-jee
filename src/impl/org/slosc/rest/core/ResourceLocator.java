package org.slosc.rest.core;

import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.net.URI;
import java.net.URISyntaxException;
import java.lang.reflect.Method;
import java.lang.reflect.InvocationTargetException;

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

    class UriTemplateRegEx {
        private String path;
        private Map<Integer, String> varibalesIndx;
        private Map<String, String> varibales;
        private Class forClazz;
        private Method forMethod;
        private String mainPath;

        UriTemplateRegEx(String path, Map varibalesIndx) {
            this.path = path;
            this.varibalesIndx = varibalesIndx;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map getVaribales() {
            return varibales;
        }

        public void setVaribales(Map varibales) {
            this.varibales = varibales;
        }

        public Class getForClazz() {
            return forClazz;
        }

        public void setForClazz(Class forClazz) {
            this.forClazz = forClazz;
        }

        public Map<Integer, String> getVaribalesIndx() {
            return varibalesIndx;
        }

        public void setVaribalesIndx(Map<Integer, String> varibalesIndx) {
            this.varibalesIndx = varibalesIndx;
        }

        public String getMainPath() {
            return mainPath;
        }

        public void setMainPath(String mainPath) {
            this.mainPath = mainPath;
        }

        public Method getForMethod() {
            return forMethod;
        }

        public void setForMethod(Method forMethod) {
            this.forMethod = forMethod;
        }
    }

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
        resolve(c, e);

        /*
        (c) Filter E by matching each member against U as follows:
            - Remove members that do not match U.
            - Remove members for which the final regular expression capturing group (henceforth simply
            referred to as a capturing group) value is neither empty nor ‘/’ and the class associated with 
            R(Tclass) had no sub-resource methods or locators.
         */
        Set<String> remove = new HashSet<String>();
        for(String path: e){
            if(!match(u, path, classEntries))
                remove.add(path);
        }
        e.removeAll(remove);

        if(e.size() == 0) {
            System.err.println("ERROR: No resource found ... ");
            throw new WebApplicationException();
        }

        if(e.size() > 1) {
            System.err.println("WARN: There exist more than one matching resource for the uri: "+u);
        }

        Iterator itr = e.iterator();
        List<UriTemplateRegEx> reg = classEntries.get(itr.next());

        //ok we have the required class, and variables as of now.
        Object res = null;
        Class resource =  reg.get(0).getForClazz();
        //find the what path string applicable for method level
        String subPath =  u.substring(reg.get(0).getMainPath().length());

        Method[] methods = resource.getMethods();
        Set<Method> reqMethods = new HashSet<Method>();

        for(Method m:methods){
            if(m.isAnnotationPresent(Path.class)) reqMethods.add(m);
        }
        
        e = new HashSet<String>();
        resolveMethods(reqMethods, e);

        remove = new HashSet<String>();
        for(String path: e){
            if(!match(subPath, path, methodEntries))
                remove.add(path);
        }
        e.removeAll(remove);

        if(e.size() == 0) {
            System.err.println("ERROR: No resource method found ... ");
            throw new WebApplicationException();
        }

        if(e.size() > 1) {
            System.err.println("WARN: There exist more than one matching resource for the uri: "+u);
        }

        itr = e.iterator();
        reg = methodEntries.get(itr.next());
        Method methodToInvoke =  reg.get(0).getForMethod();
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
                matches = uriTemplateRegEx.getVaribalesIndx().size() == gcount;

                
                if(matches){
                    Map<String, String> varibales = new HashMap<String, String>();
                    for(int i=1; i <= gcount;i++){
                        varibales.put(uriTemplateRegEx.getVaribalesIndx().get(i-1), m.group(i));
                    }
                    uriTemplateRegEx.setVaribales(varibales);
                }
            }
            uriTemplateRegEx.setMainPath(m.group(0));
        }
        return matches;
    }

//        int start = path.indexOf('{');
//        while(start > 0){
//
//            if(!uri.regionMatches(true, 0, path, start - 1 , start - 1)) return false;
//            uri = uri.substring(start);
//            int end = path.indexOf('}');
//            String var = path.substring(0, end - 1);
//
//            if (!matchVars(uri, var, reg)) return false;
//            path = path.substring(end+1);
//            start = path.indexOf('{');
//        }

//        return uri.equals(path);
//    }

//    private boolean matchVars(String uri, String var, final List<UriTemplateRegEx> reg) {
//        for(UriTemplateRegEx uriTempRegex: reg){
//            Map<String, String> vars = uriTempRegex.getVaribales();
//            if(!vars.containsKey(var)) return false;
//            String regex = vars.get(var);
//            uri.matches(regex);
//
//        }
//
//
//        return true;
//    }

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

//        if(!Character.isLetter(uriTemplate.charAt(0)) && uriTemplate.charAt(0) != '_') throw new IllegalArgumentException("Invalid path");

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
            varibalesIndx.put(groupIndx, variable);
            pathStr.append(regex);

            start = uriTemplate.indexOf('{');
        }
        if(pathStr.charAt(0) != '/') pathStr = pathStr.append('/');
        if(pathStr.charAt(pathStr.length() - 1) != '/') pathStr = pathStr.append('/');
        
        return new UriTemplateRegEx(pathStr.toString(), varibalesIndx);
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

//    private String encode(String str){
//       Charset utf8 = Charset.forName("UTF-8");
//
//
//       for(int i=0;i<str.length();i++){
//            char c = str.charAt(i);
//       }
//       return "";
//    }


    /**
     * A root resource class is anchored in URI space using the @Path annotation. The value of the annotation is
     * a relative URI path template whose base URI is provided by the deployment context.
     * A URI path template is a string with zero or more embedded parameters that, when values are substituted
     * for all the parameters, is a valid URI path. The Javadoc for the @Path annotation describes their syntax
     * {@link javax.ws.rs.Path}
     *
     * Template parameters can optionally specify the regular expression used to match their values. 
     * TODO For now only simple pattens are allowed
     */
    private void uriTemplate(){

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

}
