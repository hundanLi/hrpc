package com.hrpc.netty.stub;

import com.hrpc.api.*;
import com.hrpc.netty.client.NettyHrpcClient;
import com.hrpc.netty.serializer.HrpcPayloadSerializer;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.net.SocketAddress;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 11:36
 */
public class HrpcStubFactory {

    private final NettyHrpcClient nettyHrpcClient;

    private final HrpcPayloadSerializer payloadSerializer;

    private final HrpcDiscovery hrpcDiscovery;

    private final Map<String, RequestIdGenerator> requestIdGeneratorMap = new ConcurrentHashMap<>();

    public HrpcStubFactory(NettyHrpcClient nettyHrpcClient, HrpcPayloadSerializer payloadSerializer, HrpcDiscovery hrpcDiscovery) {
        this.nettyHrpcClient = nettyHrpcClient;
        this.payloadSerializer = payloadSerializer;
        this.hrpcDiscovery = hrpcDiscovery;
    }

    public <T> T createStub(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, new InvocationHandler() {
                    @Override
                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                        String serviceName = clazz.getName();
                        String methodName = method.getName();
                        Class<?>[] parameterTypes = method.getParameterTypes();
                        SocketAddress serviceAddress = hrpcDiscovery.getInstanceAddress(serviceName);
                        HrpcTransport transport = nettyHrpcClient.createTransport(serviceAddress, 3000);
                        HrpcRequest hrpcRequest = createRequest(serviceName, methodName, args, parameterTypes);
                        HrpcResponse hrpcResponse = transport.send(hrpcRequest).get();
                        if (hrpcResponse.getHeader().getCode() == 0) {
                            HrpcPayload payload = payloadSerializer.deserialize(hrpcResponse.getPayload(), HrpcPayload.class);
                            return payload.getResult();
                        } else {
                            return null;
                        }
                    }
                });
    }


    private HrpcRequest createRequest(String serviceId, String method, Object[] args, Class<?>[] argTypes) throws Exception {
        RequestIdGenerator requestIdGen = requestIdGeneratorMap.compute(serviceId, (key, requestIdGenerator) -> requestIdGenerator != null ? requestIdGenerator : new RequestIdGenerator());
        HrpcRequest hrpcRequest = new HrpcRequest();
        hrpcRequest.setHeader(new HrpcHeader(requestIdGen.nextId(), 1));
        HrpcPayload hrpcPayload = new HrpcPayload();
        hrpcPayload.setServiceId(serviceId);
        hrpcPayload.setServiceMethod(method);
        hrpcPayload.setParameters(Arrays.asList(args));
        hrpcPayload.setParameterTypes(Arrays.asList(argTypes));
        byte[] payload = payloadSerializer.serialize(hrpcPayload);
        hrpcRequest.setPayload(payload);
        return hrpcRequest;
    }

}
