package com.sammyyx;

import java.io.Serializable;

/**
 * User: sammy
 * Date: 2021/5/5
 * Time: 14:55
 */
public class RpcResponse implements Serializable {
    private Object result;
    public RpcResponse(Object result) {
        this.result = result;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
