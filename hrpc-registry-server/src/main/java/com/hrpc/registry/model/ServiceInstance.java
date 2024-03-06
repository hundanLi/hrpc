package com.hrpc.registry.model;

import java.util.Map;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 21:20
 */
public class ServiceInstance {

    private String serviceId;
    private String host;
    private Integer port;
    private Integer createTime;
    private Map<String, String> metadata;

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Integer createTime) {
        this.createTime = createTime;
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata;
    }
}
