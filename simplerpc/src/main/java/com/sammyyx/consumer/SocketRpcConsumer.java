package com.sammyyx.consumer;

import com.sammyyx.RpcRequest;
import com.sammyyx.common.util.LogUtil;
import com.sammyyx.tracer.TracerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * User: sammy
 * Date: 2021/5/5
 * Time: 12:05
 */
public class SocketRpcConsumer implements InvocationHandler {


    private static final Logger logger = LogManager.getLogger(SocketRpcConsumer.class);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        // 建立与 server 的连接
        Socket socket = new Socket("127.0.0.1", 7979);

        LogUtil.info(logger, "connected to client");

        // 封装 rpc request
        String interfaceName = proxy.getClass().getInterfaces()[0].getName();
        String methodName = method.getName();
        List<Object> objectArgs = Arrays.asList(args);
        RpcRequest rpcRequest = new RpcRequest(interfaceName, methodName, objectArgs, TracerUtil.getTraceId());

        // 序列化
        ByteArrayOutputStream requestBAOS = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(requestBAOS);
        objectOutputStream.writeObject(rpcRequest);

        // 发送 rpc 请求
        OutputStream outputStream = socket.getOutputStream();
        outputStream.write(requestBAOS.toByteArray());
        outputStream.flush();
        socket.shutdownOutput();

        // 接受 rpc 响应
        ByteArrayOutputStream responseBAOS = new ByteArrayOutputStream();
        InputStream inputStream = socket.getInputStream();
        int len;
        byte[] buffer = new byte[1024];
        while ((len = inputStream.read(buffer)) != -1) {
            responseBAOS.write(buffer, 0, len);
        }
        socket.close();

        // 反序列化
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(responseBAOS.toByteArray()));

        // 调用结果返回
        return objectInputStream.readObject();
    }
}
