package org.slosc.rest.core.resource;

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

    private List<String> lookupPaths = new ArrayList<String>();

    private Set<Class> resourceClasses = new HashSet<Class>();

    public void addLookupPath(String path){
        lookupPaths.add(path);
    }

    public void lookup() throws Exception {

       for(String path : lookupPaths){
            lookInDir(new File(path));
       }
    }

    private void lookInDir(File dir) throws Exception{
       File[] lst = dir.listFiles();

       for(File f:lst){
           if(f.isDirectory()) lookInDir(f);
           
           if (f.getName().endsWith(".jar")){
               processJarFile(f);
           }

           if(f.getName().endsWith(".class")){
                processClassFile(f);     
           }
       }
    }

    private void processJarFile(File jarFile) throws Exception{
        JarFile jar = new JarFile(jarFile);

        Enumeration<JarEntry> entries = jar.entries();
        while(entries.hasMoreElements()){
            JarEntry entry = entries.nextElement();
            entry.getName().endsWith(".class");
            DataInputStream in = new DataInputStream(new BufferedInputStream(jar.getInputStream(entry), BUFSIZE));
            ResourceClassParser par = new ResourceClassParser(in);
            par.parse();
            String className = par.getClazzName();
            if(className != null) resourceClasses.add(Class.forName(className.replaceAll("/", ".")));

        }
    }

    public Set<Class> getResourceClasses() {
        return resourceClasses;
    }

    private void processClassFile(File classFile){

    }
    
    class LookupFileFilter  implements FileFilter {
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".class") ;
        }
    }

}
