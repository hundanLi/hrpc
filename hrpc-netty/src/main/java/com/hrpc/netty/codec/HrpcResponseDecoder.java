package com.hrpc.netty.codec;

import com.hrpc.api.HrpcRespHeader;
import com.hrpc.api.HrpcResponse;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 13:46
 */
public class HrpcResponseDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {

        int requestId = in.readInt();
        int version = in.readInt();
        int code = in.readInt();
        HrpcResponse hrpcResponse = new HrpcResponse();
        HrpcRespHeader hrpcRespHeader = new HrpcRespHeader();
        hrpcRespHeader.setCode(code);
        hrpcRespHeader.setRequestId(requestId);
        hrpcRespHeader.setVersion(version);
        hrpcResponse.setHeader(hrpcRespHeader);
        int payloadLen = in.readableBytes();
        if (payloadLen <= 0) {
            hrpcResponse.setPayload(new byte[0]);
        } else {
            byte[] payload = new byte[payloadLen];
            in.readBytes(payload);
            hrpcResponse.setPayload(payload);
        }
        out.add(hrpcResponse);
    }
}
