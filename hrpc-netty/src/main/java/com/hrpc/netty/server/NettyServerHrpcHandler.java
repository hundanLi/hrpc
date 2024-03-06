package com.hrpc.netty.server;

import com.hrpc.api.*;
import com.hrpc.netty.serializer.HrpcPayloadSerializer;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 17:15
 */
@ChannelHandler.Sharable
public class NettyServerHrpcHandler extends SimpleChannelInboundHandler<HrpcRequest> {


    private final HrpcRegistry hrpcRegistry;

    private final HrpcPayloadSerializer payloadSerializer;


    public NettyServerHrpcHandler(HrpcRegistry hrpcRegistry, HrpcPayloadSerializer payloadSerializer) {
        this.hrpcRegistry = hrpcRegistry;
        this.payloadSerializer = payloadSerializer;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HrpcRequest request) throws Exception {
        HrpcPayload requestPayload = payloadSerializer.deserialize(request.getPayload(), HrpcPayload.class);
        Object service = hrpcRegistry.getService(requestPayload.getServiceId());
        if (service != null) {
            Method method = service.getClass().getMethod(requestPayload.getServiceMethod(), requestPayload.getParameterTypes().toArray(new Class[0]));
            Object result = method.invoke(service, requestPayload.getParameters().toArray(new Object[0]));
            HrpcPayload responsePayload = new HrpcPayload();
            responsePayload.setServiceId(requestPayload.getServiceId());
            responsePayload.setServiceMethod(requestPayload.getServiceMethod());
            responsePayload.setResult(result);
            HrpcResponse hrpcResponse = new HrpcResponse();
            HrpcRespHeader header = new HrpcRespHeader();
            header.setRequestId(request.getHeader().getRequestId());
            header.setVersion(request.getHeader().getVersion());
            header.setCode(0);
            hrpcResponse.setHeader(header);
            hrpcResponse.setPayload(payloadSerializer.serialize(responsePayload));
            ctx.writeAndFlush(hrpcResponse).addListener(channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    ctx.channel().close();
                }
            });
        } else {
            HrpcResponse hrpcResponse = new HrpcResponse();
            HrpcRespHeader header = new HrpcRespHeader();
            header.setRequestId(request.getHeader().getRequestId());
            header.setVersion(request.getHeader().getVersion());
            header.setCode(100);
            hrpcResponse.setHeader(header);
            ctx.writeAndFlush(hrpcResponse).addListener(channelFuture -> {
                if (!channelFuture.isSuccess()) {
                    ctx.channel().close();
                }
            });

        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.channel().close();
    }
}
