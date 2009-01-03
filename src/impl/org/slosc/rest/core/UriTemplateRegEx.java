package org.slosc.rest.core;

import java.util.Map;
import java.lang.reflect.Method;

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
 *         Date    : Jan 3, 2009
 * @version: 1.0
 */
public class UriTemplateRegEx {
        private String path;
        private Map<Integer, String> varibalesIndx;
        private Map<String, String> varibales;
        private Class forClazz;
        private Method forMethod;
        private String mainPath;
        private String finalCapturingGroup;
        private int gcount;
        private int [] defaultRegexLoc = new int[25];
        private int defaultRegexLocIndx = 0;

        public UriTemplateRegEx(String path, Map varibalesIndx) {
            this.path = path;
            this.varibalesIndx = varibalesIndx;
        }

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public Map getVaribales() {
            return varibales;
        }

        public void setVaribales(Map varibales) {
            this.varibales = varibales;
        }

        public Class getForClazz() {
            return forClazz;
        }

        public void setForClazz(Class forClazz) {
            this.forClazz = forClazz;
        }

        public Map<Integer, String> getVaribalesIndx() {
            return varibalesIndx;
        }

        public void setVaribalesIndx(Map<Integer, String> varibalesIndx) {
            this.varibalesIndx = varibalesIndx;
        }

        public String getMainPath() {
            return mainPath;
        }

        public void setMainPath(String mainPath) {
            this.mainPath = mainPath;
        }

        public Method getForMethod() {
            return forMethod;
        }

        public void setForMethod(Method forMethod) {
            this.forMethod = forMethod;
        }

        public String getFinalCapturingGroup() {
            return finalCapturingGroup;
        }

        public void setFinalCapturingGroup(String finalCapturingGroup) {
            this.finalCapturingGroup = finalCapturingGroup;
        }

        public int getGcount() {
            return gcount;
        }

        public void setGcount(int gcount) {
            this.gcount = gcount;
        }

        public void addToDefaultRegexLoc(int loc) {
            if(defaultRegexLocIndx < 25)
                defaultRegexLoc[defaultRegexLocIndx++] = loc;
        }

        public int getFromDefaultRegexLoc(int indx) {
            if(indx < defaultRegexLocIndx)
                return defaultRegexLoc[indx];
            return -1;
        }

        public int getDefaultRegexLocIndx() {
            return defaultRegexLocIndx;
        }
    }
