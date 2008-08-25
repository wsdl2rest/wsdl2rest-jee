package org.slosc.wsdl2rest.util;

import java.io.FileFilter;
import java.io.File;

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


public class WSDLFileFilter  implements FileFilter {
    public boolean accept(File pathname) {
//            System.out.println("Processin wsdl: "+pathname.getName());
        return pathname.getName().endsWith(".wsdl");
    }
}
