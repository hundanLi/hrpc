package com.hrpc.registry;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hrpc.api.HrpcDiscovery;
import com.hrpc.api.ServiceInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Mono;
import reactor.netty.ByteBufMono;
import reactor.netty.http.client.HttpClient;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hundanli
 */
public class HrpcDiscoveryClient implements HrpcDiscovery {

    private final ObjectMapper objectMapper;

    private final Logger logger = LoggerFactory.getLogger(HrpcDiscoveryClient.class);

    private final HttpClient httpClient = HttpClient.create();

    private final String registryServer;

    private final String registerUri = "/registry/registerInstance";
    private final String getInstanceUri = "/registry/getInstance";
    private final String getInstancesUri = "/registry/getInstances";


    public HrpcDiscoveryClient(String registryServer) {
        this.registryServer = registryServer;
        JsonFactory jsonFactory = new JsonFactory();
        jsonFactory.enable(JsonParser.Feature.ALLOW_COMMENTS);
        jsonFactory.enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES);
        jsonFactory.enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES);
        this.objectMapper = new ObjectMapper(jsonFactory);
        this.objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
        this.objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Override
    public void registerService(ServiceInstance serviceInstance) {
        serviceInstance.setCreateTime((int) (System.currentTimeMillis() / 1000));
        try {
            String requestJson = objectMapper.writeValueAsString(serviceInstance);
            httpClient
                    .headers(headers -> headers.add("Content-Type", "application/json"))
                    .post()
                    .uri(registryServer + registerUri)
                    .send(ByteBufMono.fromString(Mono.just(requestJson)))
                    .responseSingle((response, bufMono) -> {
                        logger.info("register response status: " + response.status());
                        return bufMono.asString();
                    }).subscribe(str -> {
                logger.info("register response str: " + str);
            });
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }


    @Override
    public SocketAddress getInstanceAddress(String serviceId) {
        ServiceInstance serviceInstance = httpClient
                .get()
                .uri(registryServer + getInstanceUri + "?serviceId=" + serviceId)
                .responseSingle((response, bufMono) -> {
                    logger.info("getService response status:" + response.status());
                    return bufMono.asString();
                })
                .map(str -> {
                    try {
                        logger.debug("getService response str: " + str);
                        return objectMapper.readValue(str, ServiceInstance.class);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return new ServiceInstance();
                }).block();
        if (serviceInstance == null || serviceInstance.getHost() == null) {
            throw new IllegalStateException("no instance found for serviceId: " + serviceId);
        }
        return new InetSocketAddress(serviceInstance.getHost(), serviceInstance.getPort());
    }


    @Override
    public List<ServiceInstance> getInstances(String serviceId) {

        return httpClient
                .get()
                .uri(registryServer + getInstancesUri + "?serviceId=" + serviceId)
                .responseSingle((response, bufMono) -> {
                    logger.info("getService response status:" + response.status());
                    return bufMono.asString();
                })
                .map(str -> {
                    try {
                        logger.debug("getService response str: " + str);
                        return objectMapper.readValue(str, new TypeReference<List<ServiceInstance>>() {
                        });
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    return new ArrayList<ServiceInstance>();
                }).block();
    }


}
