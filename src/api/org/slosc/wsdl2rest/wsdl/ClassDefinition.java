package org.slosc.wsdl2rest.wsdl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lilantha
 * Date: Jun 22, 2008
 * Time: 6:14:36 PM
 * To change this template use File | Settings | File Templates.
 */
public interface ClassDefinition {

    String getPackageName();
    String getClassName();

    List<MethodInfoImpl> getMethods();
}
