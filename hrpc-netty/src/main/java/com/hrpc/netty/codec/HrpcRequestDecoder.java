package com.hrpc.netty.codec;

import com.hrpc.api.HrpcHeader;
import com.hrpc.api.HrpcRequest;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:53
 */
public class HrpcRequestDecoder extends ByteToMessageDecoder {

    private final int HEADER_REQUEST_ID_LENGTH = Integer.BYTES;
    private final int HEADER_VERSION_LENGTH = Integer.BYTES;
    private final int HEADER_LENGTH = HEADER_REQUEST_ID_LENGTH + HEADER_VERSION_LENGTH;


    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int len = byteBuf.readableBytes();
        if (len < HEADER_LENGTH) {
            return;
        }
        int requestId = byteBuf.readInt();
        int version = byteBuf.readInt();
        byte[] payload = new byte[len - HEADER_LENGTH];
        byteBuf.readBytes(payload);
        HrpcHeader hrpcHeader = new HrpcHeader(requestId, version);
        HrpcRequest hrpcRequest = new HrpcRequest();
        hrpcRequest.setHeader(hrpcHeader);
        hrpcRequest.setPayload(payload);
        list.add(hrpcRequest);
    }
}
