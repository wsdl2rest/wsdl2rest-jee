package org.slosc.rest.core.ext;/*
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



import org.slosc.rest.core.UriBuilderImpl;
import org.slosc.rest.core.ResponseBuilderImpl;
import org.slosc.rest.core.VariantListBuilderImpl;

import javax.ws.rs.ext.RuntimeDelegate;
import javax.ws.rs.core.*;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 13, 2008
 * @version: 1.0
 */
public class RuntimeDelegateImpl extends RuntimeDelegate {
    public UriBuilder createUriBuilder() {
        return new UriBuilderImpl();  //TODO change body of implemented method
    }

    public Response.ResponseBuilder createResponseBuilder() {
        return new ResponseBuilderImpl();  //TODO change body of implemented method
    }

    public Variant.VariantListBuilder createVariantListBuilder() {
        return new VariantListBuilderImpl();  //TODO change body of implemented method
    }

    public <T> T createEndpoint(Application application, Class<T> endpointType) throws IllegalArgumentException, UnsupportedOperationException {
        throw new UnsupportedOperationException() ;  //TODO change body of implemented method
    }

    public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type) {
        throw new UnsupportedOperationException();  //TODO change body of implemented method
    }
}
