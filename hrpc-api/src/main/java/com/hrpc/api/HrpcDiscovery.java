package com.hrpc.api;

import java.net.SocketAddress;
import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/6 15:15
 */
public interface HrpcDiscovery {


    /**
     * 注册服务实例
     *
     * @param serviceInstance 服务实例
     */
    void registerService(ServiceInstance serviceInstance);

    /**
     * 获取服务实例列表
     *
     * @param serviceId 服务id
     * @return 实例列表
     */
    List<ServiceInstance> getInstances(String serviceId);


    /**
     * 获取服务地址
     *
     * @param serviceId 服务id
     * @return 服务地址
     */
    SocketAddress getInstanceAddress(String serviceId);

}
