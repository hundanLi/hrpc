package com.hrpc.api;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 15:55
 */
public class HrpcResponse {
    private HrpcRespHeader header;
    private byte[] payload;

    public HrpcRespHeader getHeader() {
        return header;
    }

    public void setHeader(HrpcRespHeader header) {
        this.header = header;
    }

    public byte[] getPayload() {
        return payload;
    }

    public void setPayload(byte[] payload) {
        this.payload = payload;
    }
}
