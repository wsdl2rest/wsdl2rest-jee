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

import javax.ws.rs.core.*;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.net.URI;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 13, 2008
 * @version: 1.0
 */
public class ResponseBuilderImpl extends javax.ws.rs.core.Response.ResponseBuilder {
    public Response build() {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    @Override
    public Response.ResponseBuilder clone() {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder status(int status) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder entity(Object entity) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder type(MediaType type) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder type(String type) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder variant(Variant variant) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder variants(List<Variant> variants) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder language(String language) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder language(Locale language) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder location(URI location) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder contentLocation(URI location) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder tag(EntityTag tag) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder tag(String tag) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder lastModified(Date lastModified) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder cacheControl(CacheControl cacheControl) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder expires(Date expires) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder header(String name, Object value) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder cookie(NewCookie... cookies) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }
}
