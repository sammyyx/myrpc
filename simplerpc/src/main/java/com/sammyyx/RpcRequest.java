package com.sammyyx;

import com.sammyyx.tracer.TracerUtil;

import java.io.Serializable;
import java.util.List;

/**
 * User: sammy
 * Date: 2021/5/5
 * Time: 14:54
 */
public class RpcRequest implements Serializable {
    private String traceId;
    private String interfaceName;
    private String methodName;
    private List<Object> args;
    public RpcRequest(String interfaceName, String methodName, List<Object> args, String traceId) {
        this.interfaceName = interfaceName;
        this.methodName = methodName;
        this.args = args;
        this.traceId = traceId;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public List<Object> getArgs() {
        return args;
    }

    public void setArgs(List<Object> args) {
        this.args = args;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }
}
