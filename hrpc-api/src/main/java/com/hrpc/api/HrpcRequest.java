package com.hrpc.api;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 15:55
 */
public class HrpcRequest {
    private HrpcHeader header;
    private byte[] payload;

    public HrpcHeader getHeader() {
        return header;
    }

    public void setHeader(HrpcHeader header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
