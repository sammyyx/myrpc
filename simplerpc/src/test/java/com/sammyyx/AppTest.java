package com.sammyyx;

import com.sammyyx.common.util.LogUtil;
import com.sammyyx.consumer.SocketRpcConsumer;
import com.sammyyx.service.HelloWorld;
import com.sammyyx.service.HelloWorldImpl;
import com.sammyyx.provider.SocketRpcProvider;
import com.sammyyx.tracer.TracerRunnable;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.lang.reflect.Proxy;

/**
 * Unit test for simple App.
 */
public class AppTest {

    private static final Logger logger = LogManager.getLogger(AppTest.class);

    @Test
    public void startServer() throws Exception {
        SocketRpcProvider socketRpcProvider = SocketRpcProvider.getImpl();
        socketRpcProvider.addService(new HelloWorldImpl());
        socketRpcProvider.startUp(7979);
    }

    @Test
    public void testRpc() throws Exception {
        HelloWorld helloWorld = (HelloWorld) Proxy.newProxyInstance(AppTest.class.getClassLoader(), new Class[]{HelloWorld.class}, new SocketRpcConsumer());
        Thread thread1 = new Thread(new TracerRunnable(() -> {
            Object result = helloWorld.helloWorld("test");
            LogUtil.info(logger, "thread 1 executed,result={}", result);
        }));
        thread1.start();

        Thread thread2 = new Thread(new TracerRunnable(() -> {
            Object result = helloWorld.helloWorld("test1", "test2");
            LogUtil.info(logger, "thread 2 executed,result={}", result);
        }));
        thread2.start();

        Thread.sleep(1000);
    }
}
