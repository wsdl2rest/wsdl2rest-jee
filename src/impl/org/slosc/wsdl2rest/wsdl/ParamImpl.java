package org.slosc.wsdl2rest.wsdl;

/**
 * Created by IntelliJ IDEA.
 * User: lilantha
 * Date: Jun 22, 2008
 * Time: 11:38:16 PM
 * To change this template use File | Settings | File Templates.
 */
public class ParamImpl implements Param {

    private String paramType;
    private String paramName;

    public ParamImpl(String paramType, String paramName) {
        this.paramType = paramType;
        this.paramName = paramName;
    }

    public String getParamType() {
        return paramType;
    }

    public String getParamName() {
        return paramName;
    }
}
