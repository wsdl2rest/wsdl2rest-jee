package org.slosc.rest.core.resource;

import java.util.List;
import java.util.ArrayList;
import java.util.Enumeration;
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
            checkMagic(in);
            skip(in);

        }


    }

    private final static byte UTF8               =   1;
    private final static byte INTEGER            = 3;
    private final static byte FLOAT              = 4;
    private final static byte LONG               = 5;
    private final static byte DOUBLE             = 6;
    private final static byte CLASS              = 7;
    private final static byte FIELD_REF           = 9;
    private final static byte STRING             = 8;
    private final static byte METHOD_REF          = 10;
    private final static byte INTERFACE_METHID_REF = 11;
    private final static byte NAME_AND_TYPE        = 12;


    private void skip(DataInputStream in) throws Exception {
        //skip version major/minor
        in.readUnsignedShort();
        in.readUnsignedShort();

        //skip constant pool entries
        int count = in.readUnsignedShort();
        for (int i = 1; i < count; i++) {
             byte b = in.readByte(); // Read tag byte
            switch (b) {
                case CLASS:
                    in.readUnsignedShort();
                case FIELD_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();
                case METHOD_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();
                case INTERFACE_METHID_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();
                case STRING:
                    in.readUnsignedShort();
                case INTEGER:
                    in.readInt();
                case FLOAT:
                    in.readFloat();
                case LONG:
                    in.readLong();
                case DOUBLE:
                    in.readDouble();
                case NAME_AND_TYPE:
                    in.readUnsignedShort(); in.readUnsignedShort();
                case UTF8:
                    in.readUTF();
                default:
                    throw new Exception("Invalid byte tag in constant pool: " + b);
            }
        }

        //skip class info 
        in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();

        //skip interfaces
        int interfaces_count = in.readUnsignedShort();
        for (int i = 0; i < interfaces_count; i++) {
           in.readUnsignedShort();
        }

        //skip fields
        int fields_count = in.readUnsignedShort();
        for (int i = 0; i < fields_count; i++) {
            in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();  in.readUnsignedShort();
        }

        //skip methods
    }

    private final void readFields() throws IOException {

    }

    private void checkMagic(DataInputStream file) throws Exception{
        int magic = 0xCAFEBABE;
        if (file.readInt() != magic) {
            throw new Exception(" is not a Java .class file");
        }

    }

    private void processClassFile(File classFile){

    }
    
    class LookupFileFilter  implements FileFilter {
        public boolean accept(File pathname) {
            return pathname.getName().endsWith(".jar") || pathname.getName().endsWith(".class") ;
        }
    }

}
