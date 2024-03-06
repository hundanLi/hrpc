package com.hrpc.api;

import java.io.IOException;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 15:37
 */
public interface HrpcSerializer<T> {


    /**
     * 反序列化
     *
     * @param bytes byte数组
     * @param clazz 对象类型
     * @return obj对象
     */
    T deserialize(byte[] bytes, Class<T> clazz) throws IOException;

    /**
     * 序列化
     *
     * @param obj 对象
     * @return byte数组
     */
    byte[] serialize(T obj) throws Exception;

}
