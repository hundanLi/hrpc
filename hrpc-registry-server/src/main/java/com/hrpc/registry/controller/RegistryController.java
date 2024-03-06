package com.hrpc.registry.controller;

import com.hrpc.registry.model.ServiceInstance;
import com.hrpc.registry.store.RegistryStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 21:17
 */
@RestController
@RequestMapping("registry")
public class RegistryController {

    @Autowired
    private RegistryStore registryStore;

    @PostMapping("registerInstance")
    public Mono<String> registerInstance(@RequestBody ServiceInstance serviceInstance) {
        registryStore.storeInstance(serviceInstance);
        return Mono.just("ok");
    }


    @PostMapping("removeInstance")
    public Mono<String> removeInstance(@RequestBody ServiceInstance serviceInstance) {
        registryStore.removeInstance(serviceInstance);
        return Mono.just("ok");
    }


    @GetMapping("getInstance")
    public Mono<ServiceInstance> getInstance(@RequestParam("serviceId") String serviceId) {
        ServiceInstance instance = registryStore.getInstance(serviceId);
        if (instance == null) {
            instance = new ServiceInstance();
            instance.setServiceId(serviceId);
        }
        return Mono.just(instance);
    }

    @GetMapping("getInstances")
    public Mono<List<ServiceInstance>> getInstances(@RequestParam("serviceId") String serviceId) {
        List<ServiceInstance> instances = registryStore.getInstances(serviceId);
        return Mono.just(instances);
    }

}
