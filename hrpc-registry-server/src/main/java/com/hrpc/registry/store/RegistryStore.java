package com.hrpc.registry.store;

import com.hrpc.registry.model.ServiceInstance;

import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 21:20
 */
public interface RegistryStore {

    void storeInstance(ServiceInstance serviceInstance);

    void removeInstance(ServiceInstance serviceInstance);

    ServiceInstance getInstance(String serviceId);

    List<ServiceInstance> getInstances(String serviceId);
}
