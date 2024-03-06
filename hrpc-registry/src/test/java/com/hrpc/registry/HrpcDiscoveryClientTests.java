package com.hrpc.registry;

import com.hrpc.api.HrpcDiscovery;
import com.hrpc.api.ServiceInstance;
import org.junit.jupiter.api.Test;

import java.net.SocketAddress;
import java.util.HashMap;

class HrpcDiscoveryClientTests {

    @Test
    void testRegistry() {
        HrpcDiscovery hrpcDiscovery = new HrpcDiscoveryClient("http://127.0.0.1:8080");
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setServiceId("HelloService");
        serviceInstance.setHost("127.0.0.1");
        serviceInstance.setPort(9090);
        serviceInstance.setCreateTime((int) (System.currentTimeMillis() / 1000));
        serviceInstance.setTemporary(true);
        serviceInstance.setMetadata(new HashMap<>(0));
        hrpcDiscovery.registerService(serviceInstance);
        SocketAddress address = hrpcDiscovery.getInstanceAddress("HelloService");
        System.out.println(address);
    }

}
