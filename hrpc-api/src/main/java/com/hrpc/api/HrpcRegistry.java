package com.hrpc.api;

/**
 * @author hundanli
 */
public interface HrpcRegistry {

    /**
     * 注册服务实例
     * @param serviceId 服务id
     * @param service 服务对象
     * @param <T> 服务对象类型
     */
    <T> void registerService(String serviceId, T service);

    /**
     * 查询服务实例
     * @param serviceId 服务id
     * @param <T> 服务类型
     * @return 服务实例
     */
    <T> T getService(String serviceId);


}
