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

package org.slosc.rest.core.resource;


import org.objectweb.asm.*;

import javax.ws.rs.Path;
import javax.ws.rs.HttpMethod;
import javax.ws.rs.ext.Provider;
import java.io.DataInputStream;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 8, 2008
 * @version: 1.0
 */
public class ASMResourceClassParserImpl extends ResourceClassParser implements ClassVisitor {

    private ClassReader classReader = null;

    public ASMResourceClassParserImpl() {
    }

    public  void parse(DataInputStream in) throws Exception {
       this.in = in;
       clazzName = null;
       try{
           classReader = new ClassReader(in);
       }catch(Exception e){
           log.error(e);
           throw new RuntimeException(e);
       }
       classReader.accept(this, 0);
    }

    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        if((access & Opcodes.ACC_PUBLIC) != 0) tmpClazzName = name;
    }

    public void visitSource(String s, String s1) {
        //TODO change body of implemented method
    }

    public void visitOuterClass(String s, String s1, String s2) {
        //TODO change body of implemented method
    }

    /*
     * A resource class is a Java class that uses JAX-RS annotations to implement a corresponding Web resource.
     * Resource classes are POJOs that have at least one method annotated with @Path or a request method designator.
     * A request method designator is a runtime annotation that is annotated with the @HttpMethod annotation.
     * JAX-RS defines a set of request method designators for the common HTTP methods: @GET, @POST, @PUT, @DELETE,
     * @HEAD. Users may define their own custom request method designators including alternate designators for the
     * common HTTP methods.
     */
    public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
        if(desc.equals("L"+Path.class.getName().replaceAll("\\.", "/")+";")
           || desc.equals("L"+Provider.class.getName().replaceAll("\\.", "/")+";")
           || desc.equals("L"+ HttpMethod.class.getName().replaceAll("\\.", "/")+";")){

            if(tmpClazzName != null && clazzName == null && !tmpClazzName.startsWith("javax")) clazzName = tmpClazzName;
        }
        return null;
    }

    public void visitAttribute(Attribute attribute) {
        //no op
    }

    public void visitInnerClass(String s, String s1, String s2, int i) {
        //not supported at this time
    }

    public FieldVisitor visitField(int i, String s, String s1, String s2, Object o) {
        return null;
    }

    public MethodVisitor visitMethod(int i, String s, String s1, String s2, String[] strings) {
        return null;
    }

    public void visitEnd() {
    }
}
