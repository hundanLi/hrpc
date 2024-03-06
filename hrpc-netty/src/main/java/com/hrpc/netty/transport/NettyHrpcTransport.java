package com.hrpc.netty.transport;

import com.hrpc.api.HrpcRequest;
import com.hrpc.api.HrpcResponse;
import com.hrpc.api.HrpcTransport;
import com.hrpc.netty.client.HrpcRequestContext;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;

import java.util.concurrent.CompletableFuture;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:33
 */
public class NettyHrpcTransport implements HrpcTransport {
    private final Channel channel;
    private final HrpcRequestContext hrpcRequestContext;

    public NettyHrpcTransport(Channel channel, HrpcRequestContext hrpcRequestContext) {
        this.channel = channel;
        this.hrpcRequestContext = hrpcRequestContext;
    }

    @Override
    public CompletableFuture<HrpcResponse> send(HrpcRequest request) {
        // 构建返回值
        CompletableFuture<HrpcResponse> completableFuture = new CompletableFuture<>();
        try {
            // 将在途请求放到inFlightRequests中
            hrpcRequestContext.putFuture(request.getHeader().getRequestId(), completableFuture);
            // 发送命令
            channel.writeAndFlush(request).addListener((ChannelFutureListener) channelFuture -> {
                // 处理发送失败的情况
                if (!channelFuture.isSuccess()) {
                    System.err.println("request fail");
                    hrpcRequestContext.removeFuture(request.getHeader().getRequestId());
                    completableFuture.completeExceptionally(channelFuture.cause());
                    channel.close();
                }
            });
        } catch (Throwable t) {
            // 处理发送异常
            hrpcRequestContext.removeFuture(request.getHeader().getRequestId());
            completableFuture.completeExceptionally(t);
        }
        return completableFuture;
    }
}
