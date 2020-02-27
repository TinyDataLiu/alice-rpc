package com.alice.rpc.registry;

import com.alice.rpc.protocol.Protocol;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.lang.reflect.Method;
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

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
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
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.write("server error:" + cause.getMessage());
        ctx.close();
    }
}
