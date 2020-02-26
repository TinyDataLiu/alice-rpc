package com.alice.rpc.client;

import com.alice.rpc.api.HelloService;
import com.alice.rpc.entity.RpcRequest;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.Socket;

/**
 * @author liuchun
 * @date 2020/02/26  9:46
 */
public class RemoteProxy<T> implements InvocationHandler {

    private int port;
    private String host;

    public RemoteProxy(int port, String host) {
        this.port = port;
        this.host = host;
    }

    public T newInstance(Class<?> interfaces) {
        return (T) Proxy.newProxyInstance(interfaces.getClassLoader(), new Class[]{interfaces}, this);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = null;
        try (Socket socket = new Socket(host, port);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
             ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            RpcRequest request = new RpcRequest();
            request.setClassName(method.getDeclaringClass().getName());
            request.setMethod(method.getName());
            request.setArgs(args);
            objectOutputStream.writeObject(request);
            result = objectInputStream.readObject();
        }
        return result;
    }
}
