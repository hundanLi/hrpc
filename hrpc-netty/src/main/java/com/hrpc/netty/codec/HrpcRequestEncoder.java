package com.hrpc.netty.codec;

import com.hrpc.api.HrpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 13:47
 */
public class HrpcRequestEncoder extends MessageToByteEncoder<HrpcRequest> {
    @Override
    protected void encode(ChannelHandlerContext ctx, HrpcRequest request, ByteBuf out) throws Exception {
        out.writeInt(request.getHeader().getRequestId());
        out.writeInt(request.getHeader().getVersion());
        out.writeBytes(request.getPayload());
    }
}
