package com.hrpc.netty.codec;

import com.hrpc.api.HrpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:56
 */
public class HrpcResponseEncoder extends MessageToByteEncoder<HrpcResponse> {

    @Override
    protected void encode(ChannelHandlerContext ctx, HrpcResponse response, ByteBuf out) throws Exception {

        out.writeInt(response.getHeader().getRequestId());
        out.writeInt(response.getHeader().getVersion());
        out.writeInt(response.getHeader().getCode());
        out.writeBytes(response.getPayload());

    }
}
