package com.hrpc.netty.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrpc.api.HrpcPayload;
import com.hrpc.api.HrpcSerializer;

import java.io.IOException;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:21
 */
public class HrpcPayloadSerializer implements HrpcSerializer<HrpcPayload> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public HrpcPayload deserialize(byte[] bytes, Class<HrpcPayload> clazz) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }

    @Override
    public byte[] serialize(HrpcPayload obj) throws Exception {
        return objectMapper.writeValueAsBytes(obj);
    }
}
