package com.sammyyx.tracer;

import org.apache.logging.log4j.ThreadContext;

import java.util.Map;
import java.util.UUID;

/**
 * User: sammy
 * Date: 2021/5/6
 * Time: 22:28
 */
public class TracerUtil {
    private static String genTracerId() {
        return UUID.randomUUID().toString();
    }

    public static void setTraceId() {
        ThreadContext.put("traceId", genTracerId());

    }

    public static void setTraceId(String traceId) {
        ThreadContext.put("traceId", traceId);
    }

    public static String getTraceId() {
        return (String) ThreadContext.get("traceId");
    }

    public static void clearTraceId() {
        ThreadContext.remove("traceId");
    }
}
