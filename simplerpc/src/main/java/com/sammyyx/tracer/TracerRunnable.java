package com.sammyyx.tracer;

/**
 * User: sammy
 * Date: 2021/5/6
 * Time: 23:15
 */
public class TracerRunnable implements Runnable {

    private final Runnable runnable;

    private String traceId;

    public TracerRunnable(Runnable runnable) {
        this.runnable = runnable;

    }
    public TracerRunnable(Runnable runnable, String traceId) {
        this.runnable = runnable;
        this.traceId = traceId;
    }

    @Override
    public void run() {
        if (traceId != null) {
            TracerUtil.setTraceId(traceId);
        } else {
            TracerUtil.setTraceId();
        }
        this.runnable.run();
        TracerUtil.clearTraceId();
    }
}
