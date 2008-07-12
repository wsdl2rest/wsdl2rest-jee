package org.slosc.wsdl2rest.codegenerator;

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

import org.slosc.wsdl2rest.wsdl.ClassDefinition;
import org.slosc.wsdl2rest.wsdl.MethodInfo;
import org.slosc.wsdl2rest.wsdl.Param;

import java.util.List;
import java.io.*;


public class ClassGeneratorImpl implements ClassGenerator {

    private PrintWriter writer;   //do we need a stream? for now NO

    public ClassGeneratorImpl(PrintWriter writer){
        this.writer = writer;
    }

    public void generateClass(ClassDefinition clazzDef) {

        writer.println("\npackage "+clazzDef.getPackageName()+";\n\n");
        if(clazzDef.getImports() != null){
            for(String impo : clazzDef.getImports()){
              writer.println("import "+impo+";");
            }
        }
        writer.print("\n\npublic interface ");
        writer.println(clazzDef.getClassName()+" {\n");
        for(MethodInfo mInf:clazzDef.getMethods()){
            writer.print("\t"+mInf.getReturnType()+" ");
            writer.print(mInf.getMethodName()+"(");
            List<Param> params = mInf.getParams();
            if(params != null) {
                int i=0; int size = params.size();
                for(Param p : params){
                    String comma = (++i != size)?", ":"";
                    writer.print(p.getParamType()+" "+p.getParamName()+comma);
                }
            }
            String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
            writer.println(")"+excep+";");
        }
        writer.println("}\n\n\n");
    }

}
