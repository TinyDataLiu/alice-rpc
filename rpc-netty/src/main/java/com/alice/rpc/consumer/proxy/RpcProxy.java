package com.alice.rpc.consumer.proxy;

import java.lang.reflect.Proxy;

/**
 * 因为是远程调用，需要动态创建代理来完成调用
 *
 * @author liuchun
 * @date 2020/02/27  13:51
 */
public class RpcProxy {
    public static <T> T create(Class<?> clazz) {
        MethodProxy methodProxy = new MethodProxy(clazz);
        Class<?>[] interfaces = clazz.isInterface() ? new Class[]{clazz} : clazz.getInterfaces();
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(), interfaces, methodProxy);
    }
}
