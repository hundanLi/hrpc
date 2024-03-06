package com.hrpc.netty.stub;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 15:48
 */
public class RequestIdGenerator {

    private final AtomicInteger requestId = new AtomicInteger(0);

    public Integer nextId() {
        return requestId.incrementAndGet();
    }


}
