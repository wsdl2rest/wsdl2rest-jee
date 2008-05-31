package org.slosc.wsdl2rest.wsdl;

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

import javax.wsdl.*;
import javax.wsdl.factory.*;
import javax.wsdl.xml.*;
import java.util.Iterator;
import java.util.Map;

public class WSDLProcessor {

    public void process(String wsdl){

        try{
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", true);
            reader.setFeature("javax.wsdl.importDocuments", true);
            Definition def = reader.readWSDL(null, wsdl);

            prcessSchemaTypes(def);
            processMessages(def);
            processPortTypes(def);
            processBindings(def);
            processServices(def);

        }catch (WSDLException e){
            e.printStackTrace();
        }
    }

    private void prcessSchemaTypes(Definition def) {
        
    }

    public void processMessages(Definition def){
        Map messages = def.getMessages();
        for (Object o : messages.values()) {
            Message msg = (Message) o;
            if (!msg.isUndefined()) {
                System.out.println(msg.getQName());
            }
        }
    }

    private void processPortTypes(Definition def) {

    }

    private void processBindings(Definition def) {

    }

    private void processServices(Definition def) {

    }



    public static void main(String[] args) {

        if(args.length < 2){
            usage();
        }

        new WSDLProcessor().process(args[0]);
    }

    private static void usage() {
        System.out.println("Usage: wsdl2rest <wsdl>");
    }
}
