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



import javax.ws.rs.core.MultivaluedMap;
import java.util.Map;
import java.util.Set;
import java.util.Collection;
import java.util.List;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 27, 2008
 * @version: 1.0
 */
public class MultivaluedMapImpl<K,V> implements MultivaluedMap<K,V> {
    public void putSingle(K key, V value) {
        //TODO change body of implemented method
    }

    public void add(K key, V value) {
        //TODO change body of implemented method
    }

    public V getFirst(K key) {
        return null;  //TODO change body of implemented method
    }

    public int size() {
        return 0;  //TODO change body of implemented method
    }

    public boolean isEmpty() {
        return false;  //TODO change body of implemented method
    }

    public boolean containsKey(Object key) {
        return false;  //TODO change body of implemented method
    }

    public boolean containsValue(Object value) {
        return false;  //TODO change body of implemented method
    }

    public List<V> get(Object key) {
        return null;  //TODO change body of implemented method
    }

    public List<V> put(K key, List<V> value) {
        return null;  //TODO change body of implemented method
    }

    public List<V> remove(Object key) {
        return null;  //TODO change body of implemented method
    }

    public void putAll(Map<? extends K, ? extends List<V>> t) {
        //TODO change body of implemented method
    }

    public void clear() {
        //TODO change body of implemented method
    }

    public Set<K> keySet() {
        return null;  //TODO change body of implemented method
    }

    public Collection<List<V>> values() {
        return null;  //TODO change body of implemented method
    }

    public Set<Entry<K, List<V>>> entrySet() {
        return null;  //TODO change body of implemented method
    }
}
