package org.slosc.wsdl2rest.service;

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

import java.util.List;

class MetaInfoImpl implements MetaInfo {
    
    private List<String> resources;
    private String preferredResource;
    private String httpMethod;
    private String preferredHttpMethod;
    private String mimeType;
    private String preferredMimeType;
    private boolean pathParam;
	
    public String getHttpMethod() {
		return httpMethod;
	}

    public void setPreferredHttpMethod(String defaultHttpMethod) {
        this.preferredHttpMethod = defaultHttpMethod;
    }

    public void setPreferredMimeType(String defaultMimeType) {
        this.preferredMimeType = defaultMimeType;
    }

    public void setHttpMethod(String httpMethod) {
		this.httpMethod = httpMethod;
	}

	public String getMimeType() {
		return mimeType;
	}

    public String getPreferredResource() {
        return preferredResource;
    }

    public void setPreferredResource(String defaultResource) {
        this.preferredResource = defaultResource;
    }

    public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public List<String> getResources() {
		return resources;
	}

    public void setResources(List<String> resources) {
		this.resources = resources;
	}

    public String getPreferredHttpMethod() {
        return preferredHttpMethod;
    }

    public String getPreferredMimeType() {
        return preferredMimeType;
    }

    public boolean isPathParam() {
        return pathParam;
    }

    public void setPathParam(boolean pathParam) {
        this.pathParam = pathParam;
    }
}
