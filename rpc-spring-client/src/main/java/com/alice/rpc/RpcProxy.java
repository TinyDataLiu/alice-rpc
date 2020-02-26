package com.alice.rpc;

import com.alice.rpc.req.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author liuchun
 * @date 2020/02/26  12:52
 */
public class RpcProxy implements InvocationHandler {

    private String host;
    private int port;


    public RpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
    }

    /**
     * 创建远程代理对象实例
     *
     * @param interfaces
     * @param <T>
     * @return
     */
    public <T> T newInstance(final Class<T> interfaces) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        Socket socket = new Socket(host, port);
        try (
                ObjectInputStream inputStream = new ObjectInputStream(socket.getInputStream());
                ObjectOutputStream outputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            RpcRequest request = new RpcRequest();
            request.setArgs(args);
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethodName(method.getName());
            outputStream.writeObject(request);
            outputStream.flush();
            result = inputStream.readObject();
        }
        return result;
    }
}
