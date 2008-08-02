package org.slosc.wsdl2rest.codegenerator.jsr311;

import org.slosc.wsdl2rest.codegenerator.ClassGeneratorImpl;
import org.slosc.wsdl2rest.wsdl.ClassDefinition;
import org.slosc.wsdl2rest.wsdl.MethodInfo;
import org.slosc.wsdl2rest.wsdl.Param;

import java.util.List;

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


public class JSR311ClassGenerator extends ClassGeneratorImpl {

    protected void writeServiceClass(ClassDefinition clazzDef){
        if(clazzDef.getClassName() != null){
            //write jsr-311 annotations
            writer.println("@Path(\"/"+clazzDef.getClassName().toLowerCase()+"/");
            writer.println("@ProduceMime(\"application/xml\")");

            writer.println("\n\npublic class "+clazzDef.getClassName()+" {\n");
            writeMethods(clazzDef.getMethods());
            writer.println("}\n\n\n");
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods){
        if(methods == null) return;
        for(MethodInfo mInf:methods){
            //write jsr-311 annotations
            
            writer.print("\tpublic "+mInf.getReturnType()+" ");
            writer.print(mInf.getMethodName()+"(");
            writeParams(mInf.getParams());
            String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
            writer.println(")"+excep+";");
        }

    }

     protected void writeParams(List<Param> params){
            if(params != null) {
                int i=0; int size = params.size();
                for(Param p : params){
                    String comma = (++i != size)?", ":"";
                    //write jsr-311 annotations
                    writer.print(p.getParamType()+" "+p.getParamName()+comma);
                }
            }
     }
    
}
