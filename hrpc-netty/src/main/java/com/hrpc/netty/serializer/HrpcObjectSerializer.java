package com.hrpc.netty.serializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hrpc.api.HrpcSerializer;

import java.io.IOException;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 11:14
 */
public class HrpcObjectSerializer implements HrpcSerializer<Object> {

    private final ObjectMapper objectMapper = new ObjectMapper();


    @Override
    public Object deserialize(byte[] bytes, Class<Object> clazz) throws IOException {
        return objectMapper.readValue(bytes, clazz);
    }

    @Override
    public byte[] serialize(Object obj) throws Exception {
        return objectMapper.writeValueAsBytes(obj);
    }
}
