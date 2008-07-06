package org.slosc.wsdl2rest.mappings;

/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements. See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership. The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

import junit.framework.*;
import org.slosc.wsdl2rest.wsdl.WSDLProcessor;
import org.slosc.wsdl2rest.wsdl.ClassDefinition;
import org.slosc.wsdl2rest.wsdl.MethodInfo;
import org.slosc.wsdl2rest.wsdl.Param;
import org.slosc.wsdl2rest.mappings.ResourceMapperImp;

import java.util.List;
import java.io.File;
import java.io.FileFilter;

public class ResouceMapperTest extends TestCase {
    WSDLProcessor wsdlProcessor;

    class WSDLFiles implements FileFilter {
        public boolean accept(File pathname) {
//            System.out.println("Processin wsdl: "+pathname.getName());
            return pathname.getName().endsWith(".wsdl");
        }
    }

    public void testProcess() throws Exception {
//        System.out.println("testProcess:");
        String loc = System.getProperty("org.slosc.wsdl2rest.wsdl.wsdlLocations");
        if(loc == null ) {
            System.out.println("No location for wsdls");
            return;
        }
//        System.out.println("wsdls location: "+loc);
        File wsdlLoc = new File(loc);
        if(!wsdlLoc.isDirectory()) return;

        File [] files = wsdlLoc.listFiles(new WSDLFiles());

        WSDLProcessor wsdlProcessor = new WSDLProcessor();

        for(File f:files){
            wsdlProcessor.process(f.getAbsolutePath(), "testUName", "testPassword");
            List<ClassDefinition> svcClasses = wsdlProcessor.getTypeDefs();

            for(ClassDefinition clazzDef : svcClasses){
                System.out.println("\npackage "+clazzDef.getPackageName()+";");
                // Get resources of package name
                ResourceMapperImp rm1 = new ResourceMapperImp(clazzDef.getPackageName());
                System.out.print("Resources for " + clazzDef.getPackageName() + ":");
                System.out.println(rm1.toString() + "\n\n");
                
                if(clazzDef.getImports() != null){
                    for(String impo : clazzDef.getImports()){
                      System.out.println("import "+impo+";");
                      // Get resources of imports
                      ResourceMapperImp rm2 = new ResourceMapperImp(impo);
                      System.out.print("Resources for " + impo + ":");
                      System.out.println(rm2.toString());
                      
                    }
                }
                System.out.print("\n\npublic interface ");
                System.out.println(clazzDef.getClassName()+" {\n");
                for(MethodInfo mInf:clazzDef.getMethods()){
                    System.out.print("\t"+mInf.getReturnType()+" ");
                    System.out.print(mInf.getMethodName()+"(");
                    List<Param> params = mInf.getParams();
                    if(params != null) {
                        int i=0; int size = params.size();
                        for(Param p : params){
                            String comma = (++i != size)?", ":"";
                            System.out.print(p.getParamType()+" "+p.getParamName()+comma);
                        }
                    }
                    String excep = mInf.getExceptionType() != null?(" throws "+ mInf.getExceptionType()):"";
                    System.out.println(")"+excep+";");
                    // Get resources of methods
                    ResourceMapperImp rm3 = new ResourceMapperImp(mInf.getMethodName());
                    System.out.print("\t"+"Resources for " + mInf.getMethodName() + ":");
                    System.out.println(rm3.toString());
                }
                System.out.println("}");
                // Get resources of public interfaces
                ResourceMapperImp rm4 = new ResourceMapperImp(clazzDef.getClassName());
                System.out.print("Resources for " + clazzDef.getClassName() + ":");
                System.out.println(rm4.toString()+"\n\n\n");
            }
        }
    }
}