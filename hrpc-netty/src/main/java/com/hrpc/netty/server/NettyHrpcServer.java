package com.hrpc.netty.server;

import com.hrpc.netty.codec.HrpcRequestDecoder;
import com.hrpc.netty.codec.HrpcResponseEncoder;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:49
 */
public class NettyHrpcServer {

    private final NettyServerHrpcHandler nettyServerHrpcHandler;
    public NettyHrpcServer(NettyServerHrpcHandler nettyServerHrpcHandler) {
        this.nettyServerHrpcHandler = nettyServerHrpcHandler;
    }

    public void start() {

        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new HrpcRequestDecoder())
                                .addLast(new HrpcResponseEncoder())
                                .addLast(nettyServerHrpcHandler);
                    }
                });
        try {
            ChannelFuture channelFuture = bootstrap.bind(8579).sync();
            System.out.println("netty hrpc server started on port: " + 8579);
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
