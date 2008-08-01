package org.slosc.wsdl2rest;

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

import org.slosc.wsdl2rest.wsdl.WSDLProcessor;
import org.slosc.wsdl2rest.wsdl.ClassDefinition;
import org.slosc.wsdl2rest.mappings.ResourceMapper;
import org.slosc.wsdl2rest.mappings.ResourceMapperImp;
import org.slosc.wsdl2rest.codegenerator.ClassGenerator;
import org.slosc.wsdl2rest.codegenerator.ClassGeneratorImpl;
import org.slosc.wsdl2rest.util.MessageWriter;
import org.slosc.wsdl2rest.util.ConsoleMessageWriter;

import java.util.List;
import java.io.*;


public class Wsdl2Rest {

    MessageWriter msgWriter = new ConsoleMessageWriter();

    public static void main(String [] args){

        if(args.length < 4){
            usage();
        }

        Wsdl2Rest wsdl2rest = new Wsdl2Rest();

        wsdl2rest.process(args);

    }

    public void process(String... args){
        WSDLProcessor wsdlProcessor = new WSDLProcessor();
        wsdlProcessor.process(args[0], args[1], args[2]);
        List<ClassDefinition> svcClasses = wsdlProcessor.getTypeDefs();

        // Assign resources to Class, method and parameter definitions.
        ResourceMapperImp.assignResources(svcClasses);
        
        File clazzFileLocation = new File(args[3]);
        if(!clazzFileLocation.exists()) msgWriter.write(MessageWriter.TYPE.WARN, "Existing files will be over writtern ...");

        clazzFileLocation.delete();
        clazzFileLocation.mkdirs();

        ClassGenerator gen;
        String outLocation = args[3] + File.separator;

        for(ClassDefinition clazzDef : svcClasses){
            String packageName = clazzDef.getPackageName();
            packageName = packageName.replace('.', File.separatorChar);
            File clazzFile = new File(outLocation +packageName+File.separatorChar);
            clazzFile.mkdirs();
            
            PrintWriter out = null;
            try {
                clazzFile = new File(outLocation +packageName+File.separatorChar+clazzDef.getClassName()+".java");
                out = new PrintWriter(new BufferedWriter(new FileWriter(clazzFile)));
            } catch (IOException e) {
                e.printStackTrace();  
            }

            gen = new ClassGeneratorImpl(out);
            gen.generateClass(clazzDef);

            out.close();
        }

    }

    private static void usage() {
        System.out.println("Usage: java -cp <classpath> org.slosc.wsdl2rest.Wsdl2Rest <wsdl-file> <username> <password>");
    }
}
