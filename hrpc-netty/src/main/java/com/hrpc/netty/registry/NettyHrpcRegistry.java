package com.hrpc.netty.registry;

import com.hrpc.api.HrpcDiscovery;
import com.hrpc.api.HrpcRegistry;
import com.hrpc.api.ServiceInstance;
import com.hrpc.registry.HrpcDiscoveryClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/4 16:39
 */
public class NettyHrpcRegistry implements HrpcRegistry, HrpcDiscovery {


    private final Map<String, Object> serviceMap = new ConcurrentHashMap<>();

    private final HrpcDiscoveryClient hrpcDiscoveryClient = new HrpcDiscoveryClient("http://127.0.0.1:8080");

    @Override
    public <T> void registerService(String serviceId, T service) {
        this.serviceMap.put(serviceId, service);
    }

    @Override
    public <T> T getService(String serviceId) {
        return (T) this.serviceMap.get(serviceId);
    }


    @Override
    public void registerService(ServiceInstance serviceInstance) {
        hrpcDiscoveryClient.registerService(serviceInstance);
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        return hrpcDiscoveryClient.getInstances(serviceId);
    }

    @Override
    public SocketAddress getInstanceAddress(String serviceId) {
        return hrpcDiscoveryClient.getInstanceAddress(serviceId);
    }
}
