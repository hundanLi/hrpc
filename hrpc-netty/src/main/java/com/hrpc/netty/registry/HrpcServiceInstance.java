package com.hrpc.netty.registry;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:40
 */
public class HrpcServiceInstance {
    private final String host;
    private final Integer port;

    public HrpcServiceInstance(String host, Integer port) {
        this.host = host;
        this.port = port;
    }

    public String getHost() {
        return host;
    }

    public Integer getPort() {
        return port;
    }
}
