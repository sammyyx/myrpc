package com.sammyyx.provider;

import com.sammyyx.RpcRequest;
import com.sammyyx.common.util.LogUtil;
import com.sammyyx.tracer.TracerRunnable;
import com.sammyyx.tracer.TracerUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.reflect.Method;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * User: sammy
 * Date: 2021/5/5
 * Time: 12:11
 */
public class SocketRpcProvider {
    private static final Logger logger = LogManager.getLogger(SocketRpcProvider.class);

    private static final SocketRpcProvider socketRpcProvider = new SocketRpcProvider();

    private ExecutorService worker = Executors.newCachedThreadPool();

    private SocketRpcProvider() {

    }

    private final Map<String, Object> providers = new HashMap<>();

    public static SocketRpcProvider getImpl() {
        return socketRpcProvider;
    }

    public void addService(Object service) {
        String interfaceName = service.getClass().getInterfaces()[0].getName();
        providers.put(interfaceName, service);
    }

    public void startUp(int port) {
        try {
            // 开启监听 rpc 请求
            ServerSocket serverSocket = new ServerSocket(port);
            LogUtil.info(logger, "server started, listening at port:{}", port);
            while (true) {
                Socket clientSocket = serverSocket.accept();

                // 解析 rpc 请求
                RpcRequest request = decode(clientSocket);

                worker.submit(new TracerRunnable(() -> {
                    try {
                        handle(request, clientSocket.getOutputStream());
                    } catch (IOException e) {
                        LogUtil.error(logger, e, "handler thread close with exception,errorMsg={}", e.getMessage());
                    }
                }, request.getTraceId()));
            }
        } catch (Exception e) {
            LogUtil.error(logger, e, "server close with exception,errorMsg={}", e.getMessage());
        }
    }

    private RpcRequest decode(Socket socket) {
        RpcRequest rpcRequest = null;
        try {
            // 反序列化
            ByteArrayOutputStream requestBAOS = new ByteArrayOutputStream();
            InputStream inputStream = socket.getInputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                requestBAOS.write(buffer, 0, len);
            }
            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(requestBAOS.toByteArray()));
            rpcRequest = (RpcRequest) objectInputStream.readObject();
        } catch (Exception e) {
            LogUtil.error(logger, e, "handler thread close with exception,errorMsg={}", e.getMessage());
        }
        return rpcRequest;
    }

    private void handle(RpcRequest rpcRequest, OutputStream outputStream) {
        try {
            LogUtil.info(logger, "receive rpc call,request={}", rpcRequest);

            // 执行目标方法
            Object provider = providers.get(rpcRequest.getInterfaceName());
            Method[] methods = provider.getClass().getMethods();
            Object invokeResult = null;
            for (Method method : methods) {
                if (method.getName().equals(rpcRequest.getMethodName())) {
                    if (rpcRequest.getArgs().size() == method.getParameterCount()) {
                        invokeResult = method.invoke(provider, rpcRequest.getArgs().toArray());
                        LogUtil.info(logger, "execute target method,request={}", rpcRequest);
                    }
                }
            }

            // 序列化
            ByteArrayOutputStream responseBAOS = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(responseBAOS);
            objectOutputStream.writeObject(invokeResult);

            // 调用结果返回
            outputStream.write(responseBAOS.toByteArray());
            outputStream.flush();
            outputStream.close();

            LogUtil.info(logger, "send rpc response,result={}", invokeResult);
        } catch (Exception e) {
            LogUtil.error(logger, e, "handler thread close with exception,errorMsg={}", e.getMessage());
        }
    }
}
