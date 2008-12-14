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



import javax.ws.rs.core.Variant;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Locale;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 13, 2008
 * @version: 1.0
 */
public class VariantListBuilderImpl extends Variant.VariantListBuilder {
    public List<Variant> build() {
        throw new UnsupportedOperationException("build()");  //TODO change body of implemented method
    }

    public Variant.VariantListBuilder add() {
        throw new UnsupportedOperationException("add()");  //TODO change body of implemented method
    }

    public Variant.VariantListBuilder languages(Locale... languages) {
        throw new UnsupportedOperationException("languages()");  //TODO change body of implemented method
    }

    public Variant.VariantListBuilder encodings(String... encodings) {
        throw new UnsupportedOperationException("encodings()");  //TODO change body of implemented method
    }

    public Variant.VariantListBuilder mediaTypes(MediaType... mediaTypes) {
        throw new UnsupportedOperationException("mediaTypes()");  //TODO change body of implemented method
    }
}
