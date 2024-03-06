package com.hrpc.registry.store;

import com.hrpc.registry.model.ServiceInstance;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 21:26
 */
@Component
public class MemoryRegistryStore implements RegistryStore {

    private final Map<String, List<ServiceInstance>> serviceInstanceMap = new ConcurrentHashMap<>();

    @Override
    public void storeInstance(ServiceInstance serviceInstance) {
        List<ServiceInstance> serviceInstances = getServiceInstances(serviceInstance);
        boolean existed = false;
        for (ServiceInstance instance : serviceInstances) {
            if (instance.getHost().equals(serviceInstance.getHost()) &&
                    instance.getPort().equals(serviceInstance.getPort())) {
                instance.setCreateTime((int) (System.currentTimeMillis()/1000));
                instance.setMetadata(serviceInstance.getMetadata());
                existed = true;
                break;
            }
        }
        if (!existed) {
            serviceInstances.add(serviceInstance);
        }
    }

    @Override
    public void removeInstance(ServiceInstance serviceInstance) {
        List<ServiceInstance> serviceInstances = getServiceInstances(serviceInstance);
        Iterator<ServiceInstance> iterator = serviceInstances.iterator();
        while (iterator.hasNext()) {
            ServiceInstance instance = iterator.next();
            if (instance.getHost().equals(serviceInstance.getHost()) &&
                    instance.getPort().equals(serviceInstance.getPort())) {
                iterator.remove();
                break;
            }
        }
    }

    @Override
    public ServiceInstance getInstance(String serviceId) {
        List<ServiceInstance> serviceInstances = serviceInstanceMap.get(serviceId);
        if (CollectionUtils.isEmpty(serviceInstances)) {
            return null;
        } else {
            return serviceInstances.get(ThreadLocalRandom.current().nextInt(0, serviceInstances.size()));
        }
    }

    @Override
    public List<ServiceInstance> getInstances(String serviceId) {
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setServiceId(serviceId);
        return getServiceInstances(serviceInstance);
    }

    private List<ServiceInstance> getServiceInstances(ServiceInstance serviceInstance) {
        return serviceInstanceMap.compute(serviceInstance.getServiceId(), new BiFunction<String, List<ServiceInstance>, List<ServiceInstance>>() {
            @Override
            public List<ServiceInstance> apply(String key, List<ServiceInstance> serviceInstances) {
                return serviceInstances != null ? serviceInstances : new ArrayList<>();
            }
        });
    }
}
