package org.slosc.rest.core;

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

import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriBuilderException;
import java.net.URI;
import java.lang.reflect.Method;
import java.util.Map;


public class UriBuilderImpl extends UriBuilder {

    private URI uri;

    public UriBuilderImpl() {}

    public UriBuilder uri(URI uri) throws IllegalArgumentException {
        this.uri = uri;
        return this;
    }

    @Override
    public UriBuilder clone() {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder scheme(String scheme) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder schemeSpecificPart(String ssp) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder userInfo(String ui) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder host(String host) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder port(int port) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder replacePath(String path) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder path(String path) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder path(Class resource) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder path(Class resource, String method) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder path(Method method) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder segment(String... segments) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder replaceMatrix(String matrix) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder matrixParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder replaceMatrixParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder replaceQuery(String query) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder queryParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder replaceQueryParam(String name, Object... values) throws IllegalArgumentException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public UriBuilder fragment(String fragment) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public URI buildFromMap(Map<String, ? extends Object> values) throws IllegalArgumentException, UriBuilderException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public URI buildFromEncodedMap(Map<String, ? extends Object> values) throws IllegalArgumentException, UriBuilderException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public URI build(Object... values) throws IllegalArgumentException, UriBuilderException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public URI buildFromEncoded(Object... values) throws IllegalArgumentException, UriBuilderException {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }
}
