package com.alice.rpc.registry;

import com.alice.rpc.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;

import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在 RegistryHandler 中实现注册的具体逻辑，上面的代码，主要实现服务注册和服务调用的功能。因为所有模块创
 * 建在同一个项目中，为了简化，服务端没有采用远程调用，而是直接扫描本地 Class，然后利用反射调用。
 *
 * @author liuchun
 * @date 2020/02/27  13:23
 */
public class RegistryHandler extends ChannelInboundHandlerAdapter {
    /**
     * 用于缓存服务
     */
    private final Map<String, Object> cacheMap = new ConcurrentHashMap<>();
    private static final String PACKAGE_NAME = "com.alice.rpc.provider";

    /**
     * 构造的时候将所有需要提供服务的类缓存
     */
    public RegistryHandler() {
        scannerClass(PACKAGE_NAME);
    }

    /**
     * 扫描指定包下的类文件，并实例化
     *
     * @param packageName
     */
    private void scannerClass(String packageName) {
        URL url = this.getClass().getClassLoader().getResource(packageName.replaceAll("\\.", "/"));
        File file = new File(url.getFile());
        for (File subFile : file.listFiles()) {
            if (subFile.isDirectory()) {
                scannerClass(packageName + "." + subFile.getName());
            } else {
                try {
                    String className = packageName + "." + subFile.getName().replace(".class", "").trim();
                    Class<?> clazz = Class.forName(className);
                    Class<?> cacheName = clazz.getInterfaces()[0];
                    cacheMap.put(cacheName.getName(), clazz.newInstance());
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        //获取请求实体
        Protocol request = (Protocol) msg;
        //当客户端建立连接时，需要从自定义协议中获取信息，拿到具体的服务和实参
        if (cacheMap.containsKey(request.getClassName())) {
            Object clazz = cacheMap.get(request.getClassName());
            Method method = clazz.getClass().getMethod(request.getMethodName(), request.getTypes());
            ctx.write(method.invoke(clazz, request.getArgs()));
        } else {
            ctx.write("service not found");
        }
        ctx.flush();
        ctx.close();
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channelReadComplete");
        ctx.write("1231231232");

    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.write("server error:" + cause.getMessage());
        ctx.close();
    }
}
