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

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;

import javax.wsdl.*;
import javax.wsdl.extensions.ExtensibilityElement;
import javax.wsdl.extensions.schema.Schema;
import javax.wsdl.factory.*;
import javax.wsdl.xml.*;
import javax.xml.namespace.QName;
import java.util.*;
import java.io.File;

public class WSDLProcessor {

    private static final String xsdURI = "http://www.w3.org/2001/XMLSchema";

    private static String Types[] = {
        "string", "normalizedString", "token", "byte", "unsignedByte",
        "base64Binary", "hexBinary", "integer", "positiveInteger",
        "negativeInteger", "nonNegativeInteger", "nonPositiveInteger", "int",
        "unsignedInt", "long", "unsignedLong", "short", "unsignedShort",
        "decimal", "float", "double", "boolean", "time", "dateTime", "duration",
        "date", "gMonth", "gYear", "gYearMonth", "gDay", "gMonthDay", "Name",
        "QName", "NCName", "anyURI", "language", "ID", "IDREF", "IDREFS",
        "ENTITY", "ENTITIES", "NOTATION", "NMTOKEN", "NMTOKENS",
        "anySimpleType"
    };

    private static Map typeRegistry = new HashMap();

    public WSDLProcessor(){
        //TODO - fix this list
        typeRegistry.put("string", String.class.getName());
        typeRegistry.put("normalizedString", String.class.getName());
        typeRegistry.put("byte", "byte");
        typeRegistry.put("integer", "int");
        typeRegistry.put("long", "long");
        typeRegistry.put("float", "float");
        typeRegistry.put("double", "double");
        typeRegistry.put("short", "short");
        typeRegistry.put("boolean", "boolean");
        typeRegistry.put("unsignedShort", "short");
        typeRegistry.put("decimal", "double");
        typeRegistry.put("date", Date.class.getName());
        typeRegistry.put("time", Date.class.getName());

    }

    public Map process(String wsdlURI, String username, String password){
        try{
            File f = new File(wsdlURI);
            if(f.exists())
                wsdlURI = f.toURL().toString();
        }catch(Exception e){
            e.printStackTrace();
        }
        return process(wsdlURI);
    }

    public Map process(String wsdl){

        try{
            WSDLFactory factory = WSDLFactory.newInstance();
            WSDLReader reader = factory.newWSDLReader();
            reader.setFeature("javax.wsdl.verbose", true);
            reader.setFeature("javax.wsdl.importDocuments", true);
            Definition def = reader.readWSDL(null, wsdl);

//            processImports(def);
//            prcessSchemaTypes(def);
//            processMessages(def);
//            processPortTypes(def);
//            processBindings(def);
            processServices(def);

        }catch (WSDLException e){
            e.printStackTrace();
        }
        return null;
    }
    
    private void processImports(Definition def) {
        Map imports = def.getImports();
        for (Object o : imports.values()) {
            Import imp = (Import) o;
            Definition importDef = imp.getDefinition();

        }
    }

    private void prcessSchemaTypes(Definition def) {
        Types schemaTypes = def.getTypes();
        if(schemaTypes != null){
            Element e = schemaTypes.getDocumentationElement();
        }
    }

    public void processMessages(Definition def){
        Map messages = def.getMessages();
        System.out.println("Messages: ");
        for (Object o : messages.values()) {
            Message msg = (Message) o;
            if (!msg.isUndefined()) {
                System.out.println("\t"+msg.getQName());
            }
        }
    }

    private void processPortTypes(Definition def) {

        Map portTypes = def.getPortTypes();
        System.out.println("PortTypes: ");
        for (Object o : portTypes.values()) {
            PortType ptype = (PortType) o;
            if (!ptype.isUndefined()) {
                System.out.println("\t"+ptype.getQName());
            }
        }

    }

    private void processBindings(Definition def) {

        Map bindings = def.getBindings();
        System.out.println("Bindings: ");
        for (Object o : bindings.values()) {
            Binding  binding = (Binding) o;
            if (! binding.isUndefined()) {
                System.out.println( "\t"+binding.getQName());
            }
        }

    }

    private void processBindings(Definition def, Binding binding){

        System.out.println("\tBinding: "+binding.getQName().getLocalPart());
        processPortTypes(def, binding.getPortType());


    }

    private void processPortTypes(Definition def, PortType portTypes){

        System.out.println("\tPortType: "+portTypes.getQName().getLocalPart());
        List ops = portTypes.getOperations();
        System.out.println("\tOperations: ");
        for(int i=0; i < ops.size();i++){
            Operation oper = (Operation)ops.get(i);
            System.out.println("\t\tOperation: "+oper.getName());
            Input in = oper.getInput();
            Output out = oper.getOutput();
            Map f = oper.getFaults();


            System.out.println("\t\t\tInput: ");
            processMessages(def, in.getMessage());
            System.out.println("\t\t\tOutput: ");
            processMessages(def, out.getMessage());
            System.out.println("\t\t\tFaults: ");
            for (Object o : f.values()) {
                Fault fault = (Fault)o;
                processMessages(def, fault.getMessage());
            }
        }
    }

    public void processMessages(Definition def, Message message){
        System.out.println("\t\t\tMessage: "+message.getQName().getLocalPart());
        if(!message.isUndefined()){
           Map parts = message.getParts();
           for(Object partO: parts.values()){
               Part part = (Part)partO;
               if(part!=null){
                   System.out.println("\t\t\tPart: "+ part.getElementName());
                   prcessSchemaTypes(def, part.getElementName());
               }

           }
        }

    }



    private void prcessSchemaTypes(Definition def, QName type) {
        Types schemaTypes = def.getTypes();
        if(schemaTypes != null){
            List eeList = schemaTypes.getExtensibilityElements();

            for (Object oee : eeList) {
                Element e = ((Schema) oee).getElement();
                if(!e.getAttribute("targetNamespace").equals(type.getNamespaceURI())) continue;

                NodeList nL = e.getChildNodes();
                for(int i=0;i<nL.getLength();i++){
                    Node node = nL.item(i);
                    if(node.getNodeType() != Node.ELEMENT_NODE || !node.getLocalName().equals("element")) continue;
                    Node name = node.getAttributes().getNamedItem("name");
                    if(!name.getNodeValue().equals(type.getLocalPart())) continue;

                    Node complexTypes = ((Element)node).getElementsByTagNameNS(xsdURI,"complexType").item(0);
                    if(complexTypes == null) continue;
                    Node sequence     = ((Element)complexTypes).getElementsByTagNameNS(xsdURI,"sequence").item(0);

                    NodeList elements = ((Element)sequence).getElementsByTagNameNS(xsdURI,"element");
                    for(int k=0;k<elements.getLength();k++){
                        Node elNode = elements.item(k);
                        Node seqName = elNode.getAttributes().getNamedItem("name");
                        Node seqType = elNode.getAttributes().getNamedItem("type");

                        String typ = seqType.getNodeValue();
                        int loc    = typ.indexOf(":");

                        if(loc > 0) {
                            typ = (String)typeRegistry.get(typ.substring(loc+1));  
                        }
                        System.out.println("\t\t\tparam: "+typ+" "+seqName.getNodeValue());
                    }
                }
            }
        }
    }

    private void processServices(Definition def) {

        Map services = def.getServices();
        System.out.println("Services: ");
        for (Object o : services.values()) {
            Service svc = (Service) o;
            System.out.println("\t"+svc.getQName().getLocalPart());

            Map ports = svc.getPorts();
            for(Object port: ports.values()){
                Port p = (Port) port;
                System.out.println("\tPort: "+p.getName());
                processBindings(def, p.getBinding());

            }
        }

    }



    public static void main(String[] args) {

        if(args.length < 3){
            usage();
        }

        new WSDLProcessor().process(args[0], args[1], args[2]);
    }

    private static void usage() {
        System.out.println("Usage: wsdl2rest <wsdl>");
    }
}
