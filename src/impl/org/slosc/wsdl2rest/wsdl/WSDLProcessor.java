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
import org.apache.wsif.schema.Parser;

import javax.wsdl.*;
import javax.wsdl.extensions.UnknownExtensibilityElement;
import javax.wsdl.factory.*;
import javax.wsdl.xml.*;
import javax.xml.namespace.QName;
import java.util.*;
import java.io.File;

/**
 * Following class process WSDL document and generate a list of service interfaces & methods
 * It follows  JSR-101 specifications
 */
public class WSDLProcessor {

    private static final String xsdURI = "http://www.w3.org/2001/XMLSchema";

    //Mapping XML types to Java types
    //the JAX-RPC mapping specification does not dictate a specific Java mapping for xsd:anyType.
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

    private Map<String, ClassDefinitionImpl> serviceDef = new HashMap<String, ClassDefinitionImpl>();
    private List<ClassDefinition> typeDefs = new ArrayList<ClassDefinition>();
    private Stack svc = new Stack();
    private Stack operation = new Stack();

    public WSDLProcessor(){
        //TODO - fix this list
//        typeRegistry.put("string", String.class.getName());
//        typeRegistry.put("normalizedString", String.class.getName());
//        typeRegistry.put("byte", "byte");
//        typeRegistry.put("integer", "int");
//        typeRegistry.put("long", "long");
//        typeRegistry.put("float", "float");
//        typeRegistry.put("double", "double");
//        typeRegistry.put("short", "short");
//        typeRegistry.put("boolean", "boolean");
//        typeRegistry.put("unsignedShort", "short");
//        typeRegistry.put("decimal", "double");
//        typeRegistry.put("date", Date.class.getName());
//        typeRegistry.put("time", Date.class.getName());
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

            prcessSchemaTypes(def);
            processServices(def);

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public List<ClassDefinition> getTypeDefs() {
        return typeDefs;
    }

    public void setTypeDefs(List<ClassDefinition> typeDefs) {
        this.typeDefs = typeDefs;
    }

    private void processImports(Definition def) {
        Map imports = def.getImports();
        for (Object o : imports.values()) {
            Import imp = (Import) o;
            Definition importDef = imp.getDefinition();

        }
    }

    private void prcessSchemaTypes(Definition def) throws Exception {
        //Get ride of this once we implement our own type mappings.
        Parser.getTypeMappings(def, typeRegistry, true, null);
        Iterator itr = typeRegistry.keySet().iterator();
        System.out.println("Type mapping:");
        while(itr.hasNext()){
            String key = (String)itr.next();
            System.out.println("Key: "+key+"\tValue: "+typeRegistry.get(key));
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
        System.out.println("\tOperations: ");

        for (Object op : portTypes.getOperations()) {
            Operation oper = (Operation) op;
            System.out.println("\t\tOperation: " + oper.getName());
            
            ClassDefinitionImpl svcDef = serviceDef.get(this.svc.peek());
            svcDef.addMethod(oper.getName());

            Input in = oper.getInput();
            Output out = oper.getOutput();
            Map f = oper.getFaults();


            System.out.println("\t\t\tInput: ");
            processMessages(def, in.getMessage());
            System.out.println("\t\t\tOutput: ");
            processMessages(def, out.getMessage());
            System.out.println("\t\t\tFaults: ");
            for (Object o : f.values()) {
                Fault fault = (Fault) o;
                processMessages(def, fault.getMessage());
            }
        }
    }

    public void processMessages(Definition def, Message message){
        System.out.println("\t\t\tMessage: "+message.getQName().getLocalPart());
        if(!message.isUndefined() && message.getParts() != null){
           for(Object p: message.getParts().values()){
               Part part = (Part)p;
               if(part != null){
                   System.out.println("\t\t\tPart: "+ part.getElementName());
                   String param = (String)typeRegistry.get(part.getElementName());
                   //prcessSchemaTypes(def, part.getElementName());
               }
           }
        }
    }

   public static final String [] NS_URI_SCHEMA_XSDS ={"http://www.w3.org/1999/XMLSchema",
           "http://www.w3.org/2000/10/XMLSchema", "http://www.w3.org/2001/XMLSchema"};
  


    //TODO - fix this; this is really complex part. make it as single iteration at begin and then reused the popuplated info.
    private void prcessSchemaTypes(Definition def, QName type) {
        
        Types types = def.getTypes();
        Map imports = def.getImports();
        //TODO - add support for imported schemas
        if(types == null) return;


        for (Object oee : types.getExtensibilityElements()) {
            Element e = ((UnknownExtensibilityElement)oee).getElement();
            String ns = e.getNamespaceURI();
            //ignore anything other than schema;
            // TODO imports are not supported at this time
            if(!(e.getLocalName().equals("schema") &&
                    (NS_URI_SCHEMA_XSDS[0].equals(ns)
                    ||NS_URI_SCHEMA_XSDS[1].equals(ns)
                    ||NS_URI_SCHEMA_XSDS[2].equals(ns)))) continue;
            //ignore any other targetNamespaces for current lookup.
            String targetNamespace = e.getAttribute("targetNamespace");
            if(!targetNamespace.equals(type.getNamespaceURI())) continue;

            NodeList nL = e.getChildNodes();
            for(int i=0;i<nL.getLength();i++){
                Node node = nL.item(i);
                if(node.getNodeType() != Node.ELEMENT_NODE) continue;
                Element el = (Element)node;

                //check if this element is the one we are looking for?
                Node name = el.getAttributes().getNamedItem("name");
                if(!name.getNodeValue().equals(type.getLocalPart())) continue;

                //ok now we found the required element; now see what is the type of that element
                String elLocalName = el.getLocalName();
                if (elLocalName.equals("complexType")) {
                    //this need to map to a bean parameter
                    processSchemaComplexTypes(el, type);
				} else if (elLocalName.equals("simpleType")) {
                    processSchemaSimpleType(el, type);
				} else if (elLocalName.equals("element")) {
                    processSchemaElementType(el, type);
				} else{
                    //TODO ignore other element types for now
                }

                Node complexTypes = el.getElementsByTagNameNS(xsdURI,"complexType").item(0);
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

    private void processSchemaElementType(Element el, QName type) {

    }

    private void processSchemaSimpleType(Element el, QName type) {

    }

    private void processSchemaComplexTypes(Element el, QName type) {
        
    }

    private void processServices(Definition def) {

        String svcPackageName=def.getTargetNamespace();
        Map services = def.getServices();
        System.out.println("Services: ");
        for (Object o : services.values()) {
            Service svc = (Service) o;
            final String svcName = svc.getQName().getLocalPart();
            System.out.println("\t"+svcName);
            ClassDefinitionImpl svcDef = new ClassDefinitionImpl();
            svcDef.setClassName(svcName);
            svcDef.setPackageName(svcPackageName);
            serviceDef.put(svcName, svcDef);
            this.svc.push(svcName);

            Map ports = svc.getPorts();
            for(Object po: ports.values()){
                Port port = (Port) po;
                System.out.println("\tPort: "+port.getName());
                processBindings(def, port.getBinding());

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
