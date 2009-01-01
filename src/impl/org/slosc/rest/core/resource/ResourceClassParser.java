package org.slosc.rest.core.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

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
 *         Date    : Dec 8, 2008
 * @version: 1.0
 */

/**
 * The types u1, u2, and u4 represent an unsigned one-, two-, or four-byte quantity, respectively.
 * these types may be read by methods such as readUnsignedByte, readUnsignedShort, and readInt
 * Successive items are stored in the class file sequentially, without padding or alignment.
 *
 * ClassFile structure:
 *
 *      ClassFile {
 *      u4 magic;
 *      u2 minor_version;
 *      u2 major_version;
 *      u2 constant_pool_count;
 *      cp_info constant_pool[constant_pool_count-1];
 *      u2 access_flags;
 *      u2 this_class;
 *      u2 super_class;
 *      u2 interfaces_count;
 *      u2 interfaces[interfaces_count];
 *      u2 fields_count;
 *      field_info fields[fields_count];
 *      u2 methods_count;
 *      method_info methods[methods_count];
 *      u2 attributes_count;
 *      attribute_info attributes[attributes_count];
 *      }
 *
 * see the spec for more info: http://java.sun.com/docs/books/jvms/second_edition/ClassFileFormat-Java5.pdf
 **/
public class ResourceClassParser {

    protected String clazzName = null;
    protected String tmpClazzName = null;

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

    protected static Log log = LogFactory.getLog(ResourceClassParser.class);
    protected DataInputStream in = null;

    public ResourceClassParser() {
    }

    public  void parse(DataInputStream in) throws Exception {
        this.in = in;      
        checkMagic(in);
        skip(in);
    }

    public String getClazzName() {
        return clazzName;
    }

    private  void skip(DataInputStream in) throws Exception {
        //skip version major/minor, since jvm will handle it
        int minor = in.readUnsignedShort();
        int major = in.readUnsignedShort();

        //skip constant pool entries
        int constant_pool_count = in.readUnsignedShort();
        SymbolTable constPool = new SymbolTable();
        for (int i = 1; i < constant_pool_count; i++) {
             byte b = in.readByte(); // Read tag byte
            switch (b) {
                case CLASS:
                    in.readUnsignedShort();break;
                case FIELD_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();break;
                case METHOD_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();break;
                case INTERFACE_METHID_REF:
                    in.readUnsignedShort(); in.readUnsignedShort();break;
                case STRING:
                    in.readUnsignedShort();break;
                case INTEGER:
                    in.readInt();break;
                case FLOAT:
                    in.readFloat();break;
                case LONG:
                    in.readLong();i++;break;
                case DOUBLE:
                    in.readDouble(); i++;break;
                case NAME_AND_TYPE:
                    in.readUnsignedShort(); in.readUnsignedShort();break;
                case UTF8:
                    String val = in.readUTF();constPool.addElement(val);
                    System.out.println("UTF8 val: "+val);break;
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
            in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();
            //attribute count
           int attributes_count = in.readUnsignedShort();
           for (int j = 0; j < attributes_count; j++) {
               //name index, length of data in bytes
               int name = in.readUnsignedShort();
               int len = in.readInt();
               byte[] info = new byte[len];
               if (len > 0) in.readFully(info);
           }
        }

        //skip methods
        int methods_count = in.readUnsignedShort();
        for (int i = 0; i < methods_count; i++) {
            //access flag, name index & signature index
           in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();
           //attribute count
           int attributes_count = in.readUnsignedShort();
           for (int j = 0; j < attributes_count; j++) {
               //name index, length of data in bytes
               int name = in.readUnsignedShort();
               int len = in.readInt();
               byte[] info = new byte[len];
               if (len > 0) in.readFully(info);
           }
        }

        int attrib_count = in.readUnsignedShort();
        for (int i = 0; i < attrib_count; i++) {
            //access flag, name index & signature index
           int nameIdx = in.readUnsignedShort();
           String name = (String) constPool.elementAt(nameIdx);
           System.out.println("Class attribute: "+name);
           //attribute count
           int len = in.readInt();
           byte[] info = new byte[len];
           if (len > 0) in.readFully(info);
        }
    }

    private  void checkMagic(DataInputStream in) throws Exception{
        int magic = 0xCAFEBABE;
        if (in.readInt() != magic) {
            throw new Exception(" is not a Java .class file");
        }

    }


    class SymbolTable {
        static final int ASIZE = 128;
        static final int ABITS = 7;  // ASIZE = 2^ABITS
        static final int VSIZE = 8;
        private Object[][] objects;
        private int elements;

        public SymbolTable() {
            objects = new Object[VSIZE][];
            elements = 0;
        }

        public SymbolTable(int initialSize) {
            int vsize = ((initialSize >> ABITS) & ~(VSIZE - 1)) + VSIZE;
            objects = new Object[vsize][];
            elements = 0;
        }

        public int size() { return elements; }

        public int capacity() { return objects.length * ASIZE; }

        public Object elementAt(int i) {
            if (i < 0 || elements <= i)
                return null;

            return objects[i >> ABITS][i & (ASIZE - 1)];
        }

        public void addElement(Object value) {
            int nth = elements >> ABITS;
            int offset = elements & (ASIZE - 1);
            int len = objects.length;
            if (nth >= len) {
                Object[][] newObj = new Object[len + VSIZE][];
                System.arraycopy(objects, 0, newObj, 0, len);
                objects = newObj;
            }

            if (objects[nth] == null)
                objects[nth] = new Object[ASIZE];

            objects[nth][offset] = value;
            elements++;
        }
    }

}
