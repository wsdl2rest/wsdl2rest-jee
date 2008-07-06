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
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ResourceMapperImp implements ResourceMapper {

    private List<String> resources;
	
	public ResourceMapperImp(String resourceName) {
		resources = new ArrayList<String>();
		mapResources(resourceName);
	}
    public List<String> getResources() {
        return resources;
    }
	
	public void mapResources(String resourceName) {
        Pattern pattern = Pattern.compile("([A-Z][a-z]+)|([a-z]+)");
        Matcher matcher = pattern.matcher(resourceName);
        while (matcher.find()) {
        	if (!matcher.group().equals("")){
        		addResource(matcher.group());
        	}
        }
	}
	
	public String toString() {
		return resources.toString();
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
