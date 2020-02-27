package com.alice.rpc.registry;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * 完成服务注册
 * <p>
 * 这部分是比较核心的代码， 主要逻辑就是通过netty 将服务暴露出去。主要做了三件事
 * <p>
 * 1. 暴露一个服务地址ip + 端口
 * 2. 将我们写好的api 实现注册，这里使用一个Map存储。
 * 3. 解析远程调用的参数，并通过反射调用本地服务，并返回结果
 *
 * @author liuchun
 * @date 2020/02/27  13:08
 */
public class RpcRegistry {

    private int port;

    public RpcRegistry(int port) {
        this.port = port;
    }

    public void start() {
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap server = new ServerBootstrap();
            server.group(boosGroup, workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel channel) throws Exception {
                            ChannelPipeline pipeline = channel.pipeline();
                            /**
                             * 自定义协议解码器
                             * 入参有 5 个，分别解释如下
                             maxFrameLength：框架的最大长度。如果帧的长度大于此值，则将抛出 TooLongFrameException。
                             lengthFieldOffset：长度字段的偏移量：即对应的长度字段在整个消息数据中得位置
                             lengthFieldLength：长度字段的长度。如：长度字段是 int 型表示，那么这个值就是 4（long 型就是 8）
                             lengthAdjustment：要添加到长度字段值的补偿值
                             initialBytesToStrip：从解码帧中去除的第一个字节数
                             */
                            pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                            //自定义协议编码器
                            pipeline.addLast(new LengthFieldPrepender(4));
//                            对象参数类型编码器
                            pipeline.addLast("encoder", new ObjectEncoder());
//                            对象参数类型解码器
                            pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
//                             协议处理
                            pipeline.addLast(new RegistryHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, Boolean.TRUE);
            ChannelFuture future = server.bind(port).sync();
            System.out.println("RPC Registry start listen at " + port);
            future.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        } finally {

        }
    }

}
