package com.hrpc.netty.client;

import com.hrpc.api.HrpcTransport;
import com.hrpc.netty.codec.HrpcRequestEncoder;
import com.hrpc.netty.codec.HrpcResponseDecoder;
import com.hrpc.netty.transport.NettyHrpcTransport;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.epoll.Epoll;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.SocketAddress;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeoutException;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 13:40
 */
public class NettyHrpcClient implements AutoCloseable {

    private EventLoopGroup ioEventGroup;
    private Bootstrap bootstrap;
    private final HrpcRequestContext hrpcRequestContext;
    private final List<Channel> channels = new LinkedList<>();

    public NettyHrpcClient() {
        hrpcRequestContext = new HrpcRequestContext();
    }

    private Bootstrap newBootstrap(ChannelHandler channelHandler, EventLoopGroup ioEventGroup) {
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(Epoll.isAvailable() ? EpollSocketChannel.class : NioSocketChannel.class)
                .group(ioEventGroup)
                .handler(channelHandler)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
        return bootstrap;
    }

    public HrpcTransport createTransport(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        return new NettyHrpcTransport(createChannel(address, connectionTimeout), hrpcRequestContext);
    }

    private synchronized Channel createChannel(SocketAddress address, long connectionTimeout) throws InterruptedException, TimeoutException {
        if (address == null) {
            throw new IllegalArgumentException("address must not be null!");
        }
        if (ioEventGroup == null) {
            ioEventGroup = newIoEventGroup();
        }
        if (bootstrap == null) {
            ChannelHandler channelHandlerPipeline = newChannelHandlerPipeline();
            bootstrap = newBootstrap(channelHandlerPipeline, ioEventGroup);
        }
        ChannelFuture channelFuture;
        Channel channel;
        channelFuture = bootstrap.connect(address);
        if (!channelFuture.await(connectionTimeout)) {
            throw new TimeoutException("connect timeout: " + address);
        }
        channel = channelFuture.channel();
        if (channel == null || !channel.isActive()) {
            throw new IllegalStateException();
        }
        channels.add(channel);
        return channel;
    }

    private ChannelHandler newChannelHandlerPipeline() {
        return new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel channel) {
                channel.pipeline()
                        .addLast(new HrpcResponseDecoder())
                        .addLast(new HrpcRequestEncoder())
                        .addLast(new NettyClientHrpcHandler(hrpcRequestContext));
            }
        };
    }

    private EventLoopGroup newIoEventGroup() {

        if (Epoll.isAvailable()) {
            return new EpollEventLoopGroup();
        } else {
            return new NioEventLoopGroup();
        }
    }

    @Override
    public void close() throws Exception {
        for (Channel channel : channels) {
            if (null != channel) {
                channel.close();
            }
        }
        if (ioEventGroup != null) {
            ioEventGroup.shutdownGracefully();
        }
        hrpcRequestContext.close();
    }
}
