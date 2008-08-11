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

import org.slosc.wsdl2rest.mappings.ResourceMapper;
import java.util.List;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import org.slosc.wsdl2rest.service.ClassDefinition;
import org.slosc.wsdl2rest.service.MethodInfo;
import org.slosc.wsdl2rest.service.Param;

public class ResourceMapperImp implements ResourceMapper {

    private List<String> resources;
    private String httpGetWords 	= "([Gg]et|[Rr]ead|[Ff]etch)";
    private String httpPostWords 	= "([Pp]ost|[Aa]dd|[Cc]reate)";
    private String httpDeleteWords 	= "([Dd]elete|[Rr]emove)";
    private String httpPutWords 	= "([Pp]ut|[Uu]pdate|[Cc]hange|[Mm]odify)";
    private String httpAllWords		= httpGetWords + "|" + httpPostWords + "|" + httpDeleteWords + "|" + httpPutWords;
    
    private Pattern httpMethodPattern = Pattern.compile(httpAllWords);
    private Pattern resourcePattern = Pattern.compile("([A-Z][a-z]+)|([a-z]+)&&[^"+httpAllWords+"]");

    public ResourceMapperImp(){
        
    }
	
    public ResourceMapperImp(String resourceName) {
		resources = new ArrayList<String>();
		mapResources(resourceName);
	}
    public List<String> getResources() {
        return resources;
    }
	
	public void mapResources(String resourceName) {
//        Pattern resourcePattern = Pattern.compile("([A-Z][a-z]+)|([a-z]+)");
        Matcher resourceMatcher = resourcePattern.matcher(resourceName);
        while (resourceMatcher.find()) {
        	if (!resourceMatcher.group().equals("")){
        		addResource(resourceMatcher.group());
        	}
        }
//        Matcher httpMethodMatcher = httpMethodPattern.matcher(resourceName);
//        while (httpMethodMatcher.find()) {
//        	if (!httpMethodMatcher.group().equals("")){
//        		addResource(httpMethodMatcher.group());
//        	}
//        }
	}
	
	public String toString() {
		return resources.toString();
	}

	// This method will iterate through Class, Method and Parameter definitions
	// and assign respective resources to them
	public void assignResources(List<ClassDefinition> svcClasses) {
        for(ClassDefinition clazzDef : svcClasses){
        	// Don't break up class name
        	if (clazzDef.getClassName()!=null){
        		clazzDef.setResources(Arrays.asList(clazzDef.getClassName()));
                for(MethodInfo mInf:clazzDef.getMethods()){
                    if (mInf.getMethodName()!=null){
                        // Parse the method name
                        resources = new ArrayList<String>();
                        mapResources(mInf.getMethodName());
                        mInf.setResources(resources);

                        if(mInf.getParams()!=null){
                            for(Param p : mInf.getParams()){
                                // Don't break up parameter name
                                if (p.getParamName()!=null)
                                    p.setResources(Arrays.asList(p.getParamName()));
                            }
                        }
                    }
                }
            }
        }
	}

    private void addResources(List<String> resouces) {
        for(String str:resources) {
        	addResource(str);
        }
    }

    private void addResource(String resouce) {
    	if (!resourceExists(resouce)) {
    		this.resources.add(resouce);
    	}
    }
    
    private boolean resourceExists(String resource) {
    	for(String str:this.resources) {
    		if (str.equals(resource)) {
    			return true;
    		}
    	}
    	return false;
    }

}
