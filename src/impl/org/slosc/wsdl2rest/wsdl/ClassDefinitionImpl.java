package org.slosc.wsdl2rest.wsdl;

import java.util.List;
import java.util.ArrayList;

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

public class ClassDefinitionImpl implements ClassDefinition {
    private String packageName;
    private String className;
    private List<MethodInfoImpl> methods = new ArrayList<MethodInfoImpl>();


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public List<MethodInfoImpl> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodInfoImpl> methods) {
        this.methods = methods;
    }

    public void addMethodInfo(MethodInfoImpl method){
        for(int i=0;i<methods.size();i++){
            MethodInfoImpl mi = methods.get(i);
            if(mi.getMethodName().equals(method.getMethodName())){
                methods.set(i, method);
                return;
            }
        }
        methods.add(method);
    }

    public MethodInfoImpl getMethodInfo(String methodName){
        for(MethodInfoImpl mi : methods){
            if(mi.getMethodName().equals(methodName))
                return mi;
        }
        return null;
    }

    public MethodInfoImpl addMethod(String methodName){
        MethodInfoImpl mi = null;
        for(int i=0;i<methods.size();i++){
            mi = methods.get(i);
            if(mi.getMethodName().equals(methodName)){
                return mi;
            }
        }
        mi = new MethodInfoImpl(methodName);
        methods.add(mi);
        return mi;
    }
}
