package org.slosc.rest.core.resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.DataInputStream;
import java.io.IOException;

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
    private DataInputStream in = null;

    public ResourceClassParser(DataInputStream in){
        this.in = in;            
    }

    public  void parse() throws Exception {
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
        int count = in.readUnsignedShort();
        for (int i = 1; i < count; i++) {
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
                    in.readUTF();break;
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
        int methods_count = in.readUnsignedShort();
        for (int i = 0; i < methods_count; i++) {
            //access flag, name index & signature index
           in.readUnsignedShort(); in.readUnsignedShort(); in.readUnsignedShort();
           //attribute count
           int attributes_count = in.readUnsignedShort();
           for (int j = 0; j < attributes_count; j++) {
               //name index, length of data in bytes
               in.readUnsignedShort(); in.readInt();
           }
        }
    }

    private  void checkMagic(DataInputStream in) throws Exception{
        int magic = 0xCAFEBABE;
        if (in.readInt() != magic) {
            throw new Exception(" is not a Java .class file");
        }

    }
}
