package com.hrpc.api;

import java.util.concurrent.CompletableFuture;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 15:48
 */
public interface HrpcTransport {

    /**
     * 发送请求
     *
     * @param request 请求对象
     * @return 响应
     */
    CompletableFuture<HrpcResponse> send(HrpcRequest request);
}
