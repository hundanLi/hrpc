package com.hrpc.api;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:03
 */
public class HrpcHeader {

    private Integer requestId;
    private Integer version;

    public HrpcHeader() {
    }

    public HrpcHeader(Integer requestId, Integer version) {
        this.requestId = requestId;
        this.version = version;
    }

    public Integer getRequestId() {
        return requestId;
    }

    public void setRequestId(Integer requestId) {
        this.requestId = requestId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}
