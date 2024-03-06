package com.hrpc.netty.service.impl;

import com.hrpc.netty.service.HelloService;

/**
 * @author hundanli
 * @version 1.0.0
 * @date 2024/3/5 14:25
 */
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello(String world, Integer id) {
        String res = "Hello, " + world + " id=" + id + " -- from hrpc!";
        System.out.println("result: " + res);
        return res;
    }
}
