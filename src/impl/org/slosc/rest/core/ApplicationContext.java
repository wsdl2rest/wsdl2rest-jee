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

import java.util.Map;
import java.util.HashMap;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 * Date    : Oct 19, 2008
 * @version: 1.0
 */
public class ApplicationContext extends ApplicationConfiguration {

    private Map<String, Object> contextParams = new HashMap<String, Object>();

    public ApplicationContext() {
    }

    public ApplicationContext(ApplicationConfiguration other) {
        super(other);
    }

    public Object get(String key){
        return contextParams.get(key);
    }

    public void add(String key, Object value){
        contextParams.put(key, value);
    }
}
