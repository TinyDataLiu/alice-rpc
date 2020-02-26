package com.alice.rpc.server;

import com.alice.rpc.req.RpcRequest;
import org.springframework.stereotype.Component;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;
import java.util.Map;

/**
 * @author liuchun
 * @date 2020/02/26  11:55
 */
public class RpcInvoke implements Runnable {

    private Map<String, Object> instancesMap;
    private Socket socket;

    public RpcInvoke(Map<String, Object> instancesMap, Socket socket) {
        this.instancesMap = instancesMap;
        this.socket = socket;
    }

    @Override
    public void run() {
        System.out.println("run...");
        try (
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream())
        ) {
            RpcRequest request = (RpcRequest) objectInputStream.readObject();
            Object res = methodInvoke(request);
            objectOutputStream.writeObject(res);
            objectOutputStream.flush();
        } catch (Exception e) {

        }
    }

    private Object methodInvoke(RpcRequest request) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        String serviceName = request.getClassName();
        Object instance = null;
        if ((instance = instancesMap.get(serviceName)) == null) {
            throw new RuntimeException("service not found:" + serviceName);
        }
        Object[] args = request.getArgs();
        Class<?>[] parameterTypes = new Class[request.getArgs().length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = args[i].getClass();
        }
        Method method = instance.getClass().getMethod(request.getMethodName(), parameterTypes);
        return method.invoke(instance, args);
    }
}
