package com.hrpc.api;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:07
 */
public class HrpcRespHeader extends HrpcHeader {
    private Integer code;

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

}
