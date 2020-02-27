package com.alice.rpc.consumer.proxy;

import com.alice.rpc.protocol.Protocol;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * 远程代理
 *
 * @author liuchun
 * @date 2020/02/27  13:53
 */
public class MethodProxy implements InvocationHandler {

    private Class<?> clazz;

    public MethodProxy(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (Object.class.equals(method.getDeclaringClass())) {
            return method.invoke(this, args);
        } else {
            // 需要走远程代理
            return rpcInvoke(proxy, method, args);
        }
    }

    /**
     * 远程dialing
     *
     * @param proxy
     * @param method
     * @param args
     * @return
     */
    private Object rpcInvoke(Object proxy, Method method, Object[] args) {
// 封装请求信息，协议
        Protocol protocol = new Protocol();
        protocol.setClassName(this.clazz.getName());
        protocol.setArgs(args);
        protocol.setMethodName(method.getName());
        protocol.setTypes(method.getParameterTypes());

        final RpcProxyHandler proxyHandler = new RpcProxyHandler();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.TCP_NODELAY, Boolean.TRUE);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel channel) throws Exception {
                    channel.pipeline()
                            .addLast("frameDecoder", new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4))
                            .addLast("frameEncoder", new LengthFieldPrepender(4))
                            .addLast("encoder", new ObjectEncoder())
                            .addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)))
                            .addLast("handler", proxyHandler);
                }
            });
            
            ChannelFuture future = bootstrap.connect("localhost", 8080).sync();
            // 请求写出
            future.channel().writeAndFlush(protocol).sync();
            // 关闭
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
        return proxyHandler.getResponse();
    }
}
