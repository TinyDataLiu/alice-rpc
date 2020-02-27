package com.alice.rpc.provider;

import com.alice.rpc.api.IHello;

/**
 * @author liuchun
 * @date 2020/02/27  13:06
 */
public class HelloImpl implements IHello {
    @Override
    public String hello(String name) {
        System.out.println(String.format("hello %s", name));
        return "hello " + name;
    }
}
