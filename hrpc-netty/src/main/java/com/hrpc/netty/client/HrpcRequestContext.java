package com.hrpc.netty.client;

import com.hrpc.api.HrpcResponse;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 15:28
 */
public class HrpcRequestContext implements AutoCloseable {

    private final Map<Integer, CompletableFuture<HrpcResponse>> completableFutureMap = new ConcurrentHashMap<>();


    public void putFuture(Integer requestId, CompletableFuture<HrpcResponse> completableFuture) {
        completableFutureMap.put(requestId, completableFuture);
    }


    public CompletableFuture<HrpcResponse> getFuture(Integer requestId) {
        return completableFutureMap.get(requestId);
    }

    public CompletableFuture<HrpcResponse> removeFuture(Integer requestId) {
        return completableFutureMap.remove(requestId);
    }


    @Override
    public void close() throws Exception {
        completableFutureMap.clear();
    }
}
