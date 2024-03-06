package com.hrpc.netty;

import com.hrpc.api.ServiceInstance;
import com.hrpc.netty.registry.NettyHrpcRegistry;
import com.hrpc.netty.serializer.HrpcPayloadSerializer;
import com.hrpc.netty.server.NettyHrpcServer;
import com.hrpc.netty.server.NettyServerHrpcHandler;
import com.hrpc.netty.service.HelloService;
import com.hrpc.netty.service.impl.HelloServiceImpl;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 14:19
 */
public class HrpcServerTests {


    @Test
    void testServer() {

        String serviceName = HelloService.class.getName();
        NettyHrpcRegistry hrpcRegistry = new NettyHrpcRegistry();
        hrpcRegistry.registerService(serviceName, new HelloServiceImpl());
        ServiceInstance serviceInstance = new ServiceInstance();
        serviceInstance.setServiceId(serviceName);
        serviceInstance.setHost("127.0.0.1");
        serviceInstance.setPort(8579);
        serviceInstance.setCreateTime((int) (System.currentTimeMillis()/1000));
        serviceInstance.setTemporary(true);
        serviceInstance.setMetadata(new HashMap<>(0));
        hrpcRegistry.registerService(serviceInstance);
        HrpcPayloadSerializer payloadSerializer = new HrpcPayloadSerializer();
        NettyServerHrpcHandler serverHrpcHandler = new NettyServerHrpcHandler(hrpcRegistry, payloadSerializer);
        NettyHrpcServer nettyHrpcServer = new NettyHrpcServer(serverHrpcHandler);
        nettyHrpcServer.start();

    }

}
