package org.slosc.wsdl2rest.samples;

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

import javax.ws.rs.*;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Request;

@Path("/bookstore/{city:.+?}/")
@Produces("application/xml")
public class BookstoreWithPattern {

    @Context UriInfo uriInfo;
    @Context Request request;

    @GET
    @Path("items/{id}")
    public String getItem(@PathParam("id") String id) {
        System.out.println("In BookstoreWithPattern.getItem() method with param : "+id);
        return "testId";
    }

    @Path("{item: .+}")
    public String getItemResource(@PathParam("item") String item) {
         System.out.println("In BookstoreWithPattern.getItemResource method with param : "+item);
        return "itemResource";
    }

    @GET
    public String[] find(@QueryParam("search") String search) {
        return new String[]{"results"};
    }
}