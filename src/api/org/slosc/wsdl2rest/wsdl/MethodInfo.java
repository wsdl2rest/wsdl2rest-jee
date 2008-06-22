package org.slosc.wsdl2rest.wsdl;

import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: lilantha
 * Date: Jun 22, 2008
 * Time: 6:16:29 PM
 * To change this template use File | Settings | File Templates.
 */
public interface MethodInfo {
    String getMethodName();
    String getReturnType();
    List<Param> getParams();
    String getExceptionType();
}
