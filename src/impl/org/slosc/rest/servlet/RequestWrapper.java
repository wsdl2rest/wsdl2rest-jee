package org.slosc.rest.servlet;

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
import org.slosc.rest.core.Request;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.core.Variant;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.EntityTag;
import java.security.Principal;
import java.util.List;
import java.util.Date;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 26, 2008
 * @version: 1.0
 */
public class RequestWrapper extends Request<HttpServletRequest> {
    HttpServletRequest req;

    private RequestWrapper() {
    }

    public RequestWrapper(HttpServletRequest req){
        this.req = req;
    }

    public Principal getUserPrincipal() {
        return req.getUserPrincipal();
    }

    public boolean isUserInRole(String role) {
        return req.isUserInRole(role);
    }

    public boolean isSecure() {
        return req.isSecure();
    }

    public String getAuthenticationScheme() {
        return req.getAuthType();
    }

    public String getMethod() {
        return req.getMethod();
    }

    public Variant selectVariant(List<Variant> variants) throws IllegalArgumentException {
        throw new UnsupportedOperationException("selectVariant()");  //TODO change body of implemented method
    }

    public Response.ResponseBuilder evaluatePreconditions(EntityTag eTag) {
       throw new UnsupportedOperationException("evaluatePreconditions()");  //TODO change body of implemented method
    }

    public Response.ResponseBuilder evaluatePreconditions(Date lastModified) {
        throw new UnsupportedOperationException("evaluatePreconditions()");  //TODO change body of implemented method
    }

    public Response.ResponseBuilder evaluatePreconditions(Date lastModified, EntityTag eTag) {
        throw new UnsupportedOperationException("evaluatePreconditions()");  //TODO change body of implemented method
    }

    public String getPath() {
        return req.getPathInfo(); //TODO change body of implemented method
    }

    public String getBasePath() {
        return req.getContextPath();  //TODO change body of implemented method
    }

    public String getBaseUri() {
        throw new UnsupportedOperationException("getBaseUri()");  //TODO change body of implemented method
    }

    public String getUri() {
        throw new UnsupportedOperationException("getUri()");  //TODO change body of implemented method
    }

    public HttpServletRequest narrow() {
        return req;
    }
}