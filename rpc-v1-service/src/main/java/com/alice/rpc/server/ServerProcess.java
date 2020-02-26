package com.alice.rpc.server;

import com.alice.rpc.api.HelloService;
import com.alice.rpc.entity.RpcRequest;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

/**
 * @author liuchun
 * @date 2020/02/26  9:30
 */
public class ServerProcess implements Runnable {

    /**
     * 接收请求
     */
    private Socket socket;

    public ServerProcess(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(socket.getInputStream());
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(socket.getOutputStream())
        ) {
            // 这里我们需要知道。调用的方法，类名
            RpcRequest request = (RpcRequest) objectInputStream.readObject();
            Object o = invoke(request);
            // 将调用结构返回
            objectOutputStream.writeObject(o);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过反射调用，并且返回调用结果
     *
     * @param request
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws InvocationTargetException
     */
    private Object invoke(RpcRequest request) throws ClassNotFoundException, IllegalAccessException, InstantiationException, InvocationTargetException, NoSuchMethodException {
        Class<?> aClass = Class.forName(request.getClassName());
        //
        HelloService instance = new HelloServiceImpl();
        Class[] types = new Class[request.getArgs().length];
        for (int i = 0; i < request.getArgs().length; i++) {
            types[i] = request.getArgs()[i].getClass();
        }
        Method method = aClass.getMethod(request.getMethod(), types);
        return method.invoke(instance, request.getArgs());
    }
}
