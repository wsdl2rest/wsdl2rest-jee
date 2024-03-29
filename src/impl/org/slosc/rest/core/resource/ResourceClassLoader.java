package org.slosc.rest.core.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.*;
import java.util.jar.JarFile;
import java.util.jar.JarEntry;
import java.io.*;

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
 * Date    : Nov 1, 2008
 * @version: 1.0
 */
public class ResourceClassLoader {

    private static final int BUFSIZE = 1024;

    protected static Log log = LogFactory.getLog(ResourceClassLoader.class);

    private List<String> lookupPaths = new ArrayList<String>();

    private Set<Class<?>> resourceClasses = new HashSet<Class<?>>();

    private ResourceClassParser parser;

    public void addLookupPath(String path){
        lookupPaths.add(path);
    }

    public void lookup() throws Exception {

       for(String path : lookupPaths){
           File f = new File(path);
           if(f.isDirectory())
            lookInDir(f);
           else{
               processFile(f);
           }
       }
    }

    private void lookInDir(File dir) throws Exception{
       File[] lst = dir.listFiles();

       for(File f:lst){
           if(f.isDirectory()) lookInDir(f);
           processFile(f);
       }
    }

    private void processFile(File f) throws Exception{
       if (f.getName().endsWith(".jar")){
           processJarFile(f);
       }

       if(f.getName().endsWith(".class")){
           InputStream input = null;
           try{
               input = f.toURL().openStream();
               processClassFile(input);
           }catch (Exception e){
                log.error(e);
                throw e;
            }finally {
                try{
                    if(input != null) input.close();
                }catch (Exception e){
                    //ignore
                }
            }
       }
    }

    private void processJarFile(File jarFile) throws Exception {
        InputStream input = null;
        try{
            JarFile jar = new JarFile(jarFile);

            Enumeration<JarEntry> entries = jar.entries();
            while(entries.hasMoreElements()){
                JarEntry entry = entries.nextElement();
                if(entry.getName().endsWith(".class")){
                    input = jar.getInputStream(entry);
                    processClassFile(input);
                }
            }
        }catch (Exception e){
            log.error(e);
            throw e;
        }finally {
            try{
                if(input != null) input.close();
            }catch (Exception e){
                //ignore
            }
        }
    }

    public Set<Class<?>> getResourceClasses() {
        return resourceClasses;
    }

    public void setParser(ResourceClassParser parser) {
        this.parser = parser;
    }

    private void processClassFile(InputStream input){
        try{
        DataInputStream in = new DataInputStream(new BufferedInputStream(input, BUFSIZE));
        //TODO for now we create ASM class parser.
        if(parser!=null) parser = new ASMResourceClassParserImpl();//new ResourceClassParser();//
            
        parser.parse(in);
        String className = parser.getClazzName();
        if(className != null) resourceClasses.add(Class.forName(className.replaceAll("/", ".")));
        }catch(Exception e){
            e.printStackTrace();
            log.error(e);
        }
    }


    
    class LookupFileFilter  implements FileFilter {
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".class") ;
        }
    }

}
