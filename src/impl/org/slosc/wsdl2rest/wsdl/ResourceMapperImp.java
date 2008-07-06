package org.slosc.wsdl2rest.wsdl;

import java.util.List;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class ResourceMapperImp implements ResourceMapper{

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
