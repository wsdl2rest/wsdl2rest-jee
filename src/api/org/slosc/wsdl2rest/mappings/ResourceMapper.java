package org.slosc.wsdl2rest.mappings;

import java.util.List;

public interface ResourceMapper {
	void mapResources(String resourceName);
	List<String> getResources();
	String toString();
}
