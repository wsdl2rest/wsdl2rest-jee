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

/**
 * By default a new resource class instance is created for each request to that resource
 *
 * Matching Requests to Resource Methods
 * Prior to matching, request URIs are normalized by following the rules for case, path segment, and percent
 * encoding normalization described in section 6.2.2 of RFC 3986[5]. The normalized request URI MUST be
 * reflected in the URIs obtained from an injected UriInfo.
 *
 * Assumption: RFC 3986: 6.2.2 Syntax-Based Normalization is already done by the servlet angine utilized for this library.
 * 
 * A request is matched to the corresponding resource method or sub-resource method by comparing the normalized request
 * URI, the media type of any request entity, and the requested response entity format to the
 * metadata annotations on the resource classes and their methods. If no matching resource method or sub-resource
 * method can be found then an appropriate error response is returned.
 */
public class RequestHandler {

    public void invoke(ApplicationContext ctx) throws Exception{

        ResourceLocator rl = new ResourceLocator();
        Object o = rl.find(ctx);
        
    }
}
