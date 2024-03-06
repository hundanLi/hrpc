package com.hrpc.netty;

import com.hrpc.api.HrpcDiscovery;
import com.hrpc.netty.client.NettyHrpcClient;
import com.hrpc.netty.registry.NettyHrpcRegistry;
import com.hrpc.netty.serializer.HrpcPayloadSerializer;
import com.hrpc.netty.service.HelloService;
import com.hrpc.netty.stub.HrpcStubFactory;
import org.junit.jupiter.api.Test;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 14:19
 */
public class HrpcClientTests {

    @Test
    void testClient() {
        NettyHrpcClient nettyHrpcClient = new NettyHrpcClient();
        HrpcPayloadSerializer payloadSerializer = new HrpcPayloadSerializer();
        HrpcDiscovery hrpcDiscovery = new NettyHrpcRegistry();
        HrpcStubFactory hrpcStubFactory = new HrpcStubFactory(nettyHrpcClient, payloadSerializer, hrpcDiscovery);
        HelloService helloService = hrpcStubFactory.createStub(HelloService.class);
        for (int i = 0; i < 10000; i++) {
            String world = helloService.sayHello("world", i);
            System.out.println("get result from rpc: " + world);
        }

    }

}
