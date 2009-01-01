package org.slosc.rest.servlet;

import org.slosc.rest.core.Response;

import javax.servlet.http.HttpServletResponse;

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
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 26, 2008
 * @version: 1.0
 */
public class ResponseWrapper extends Response<HttpServletResponse> {
        HttpServletResponse res;

    private ResponseWrapper() {
    }

    public ResponseWrapper(HttpServletResponse res){
            this.res = res;
        }

        public HttpServletResponse narrow() {
            return null;  //TODO change body of implemented method
        }
}