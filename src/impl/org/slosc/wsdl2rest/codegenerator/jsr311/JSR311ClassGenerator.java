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

    public JSR311ClassGenerator(){
    }
    public JSR311ClassGenerator(String outputPath){
        super(outputPath);
    }



    protected void writeServiceClass(ClassDefinition clazzDef){
        if(clazzDef.getClassName() != null){
            //write jsr-311 annotations
            writer.println("@Path(\"/"+clazzDef.getClassName().toLowerCase()+"/\")");
            writer.println("@ProduceMime(\"application/xml\")");
            
            super.writeServiceClass(clazzDef);
        }
    }

    protected void writeMethods(List<? extends  MethodInfo> methods){
        if(methods == null) return;
        for(MethodInfo mInf:methods){
           //write jsr-311 annotations
           List<String> resouceInf = mInf.getResources();
           if(resouceInf != null && resouceInf.size() >= 2){
               if(mInf.getResources().get(0).equals("get"))
                    writer.println("\t@GET");
               else writer.println("\t@POST");
               StringBuilder path = new StringBuilder();
               for(int i=1;i<resouceInf.size();i++){
                   path.append(resouceInf.get(i));
               }
               writer.println("\t@Path(\""+path.toString().toLowerCase()+"\")");
           }
           writeMethod(mInf);
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
