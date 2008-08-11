package org.slosc.wsdl2rest.mappings;


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

import junit.framework.*;
import org.slosc.wsdl2rest.wsdl.*;
import org.slosc.wsdl2rest.mappings.ResourceMapperImp;
import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.service.MethodInfo;
import org.slosc.wsdl2rest.service.Param;

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

        WSDLProcessor wsdlProcessor = new WSDLProcessorImpl();

        for(File f:files){
            wsdlProcessor.process(f.getAbsolutePath(), "testUName", "testPassword");
            List<ClassDefinition> svcClasses = wsdlProcessor.getTypeDefs();
            
            // Assign resources
            ResourceMapper resMapper = new ResourceMapperImp();
            resMapper.assignResources(svcClasses);

            for(ClassDefinition clazzDef : svcClasses){
                System.out.println("\npackage "+clazzDef.getPackageName()+";");
                // Get resources of package name
                System.out.print("Resources for " + clazzDef.getPackageName() + ":");
                System.out.println(clazzDef.getResources().toString() + "\n\n");
                
                if(clazzDef.getImports() != null){
                    for(String impo : clazzDef.getImports()){
                      System.out.println("import "+impo+";");
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
                    System.out.print("\t"+"Resources for " + mInf.getMethodName() + ":");
                    System.out.println(mInf.getResources().toString());
                }
                System.out.println("}");
            }
        }
    }
}
