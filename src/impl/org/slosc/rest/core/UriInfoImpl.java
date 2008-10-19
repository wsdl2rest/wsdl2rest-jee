package org.slosc.rest.core;/*
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



import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.PathSegment;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.MultivaluedMap;
import java.util.List;
import java.net.URI;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Oct 19, 2008
 * @version: 1.0
 */
public class UriInfoImpl implements UriInfo {
    
    public UriInfoImpl() {
    }

    public String getPath() {
        return null;  //TODO change body of implemented method
    }

    public String getPath(boolean decode) {
        return null;  //TODO change body of implemented method
    }

    public List<PathSegment> getPathSegments() {
        return null;  //TODO change body of implemented method
    }

    public List<PathSegment> getPathSegments(boolean decode) {
        return null;  //TODO change body of implemented method
    }

    public URI getRequestUri() {
        return null;  //TODO change body of implemented method
    }

    public UriBuilder getRequestUriBuilder() {
        return null;  //TODO change body of implemented method
    }

    public URI getAbsolutePath() {
        return null;  //TODO change body of implemented method
    }

    public UriBuilder getAbsolutePathBuilder() {
        return null;  //TODO change body of implemented method
    }

    public URI getBaseUri() {
        return null;  //TODO change body of implemented method
    }

    public UriBuilder getBaseUriBuilder() {
        return null;  //TODO change body of implemented method
    }

    public MultivaluedMap<String, String> getPathParameters() {
        return null;  //TODO change body of implemented method
    }

    public MultivaluedMap<String, String> getPathParameters(boolean decode) {
        return null;  //TODO change body of implemented method
    }

    public MultivaluedMap<String, String> getQueryParameters() {
        return null;  //TODO change body of implemented method
    }

    public MultivaluedMap<String, String> getQueryParameters(boolean decode) {
        return null;  //TODO change body of implemented method
    }

    public List<String> getMatchedURIs() {
        return null;  //TODO change body of implemented method
    }

    public List<String> getMatchedURIs(boolean decode) {
        return null;  //TODO change body of implemented method
    }

    public List<Object> getMatchedResources() {
        return null;  //TODO change body of implemented method
    }
}
