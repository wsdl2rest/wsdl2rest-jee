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
import java.util.*;

/**
 * @author : Lilantha Darshana (lilantha_os@yahoo.com)
 *         Date    : Dec 27, 2008
 * @version: 1.0
 */
public class MultivaluedMapImpl<K,V> implements MultivaluedMap<K,V> {

    Map<K, List<V>> values = new HashMap<K, List<V>>();
    
    public void putSingle(K key, V value) {
        List<V> v = values.get(key);
        if(v == null) {
            v = new ArrayList<V>();
            values.put(key, v);
        }
        v.add(value);
    }

    public void add(K key, V value) {
        //TODO change body of implemented method
    }

    public V getFirst(K key) {
        List<V> v = values.get(key);
        return v.size() == 0?null:v.get(0);  
    }

    public int size() {
        return values.size();
    }

    public boolean isEmpty() {
        return values.size() == 0;  
    }

    public boolean containsKey(Object key) {
        return values.containsKey(key);
    }

    public boolean containsValue(Object value) {
        return false;  //TODO change body of implemented method
    }

    public List<V> get(Object key) {
        return values.get(key);
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
