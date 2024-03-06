package com.hrpc.netty.client;

import com.hrpc.api.HrpcResponse;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.concurrent.CompletableFuture;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 13:48
 */
@ChannelHandler.Sharable
public class NettyClientHrpcHandler extends SimpleChannelInboundHandler<HrpcResponse> {

    private final HrpcRequestContext hrpcRequestContext;

    public NettyClientHrpcHandler(HrpcRequestContext hrpcRequestContext) {
        this.hrpcRequestContext = hrpcRequestContext;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HrpcResponse response) throws Exception {
        Integer requestId = response.getHeader().getRequestId();
        System.out.println("hrpc response, requestId=" + requestId);
        CompletableFuture<HrpcResponse> completableFuture = hrpcRequestContext.removeFuture(requestId);
        completableFuture.complete(response);
        ctx.close();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
