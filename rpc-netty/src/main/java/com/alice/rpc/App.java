package com.alice.rpc;

import com.alice.rpc.registry.RpcRegistry;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        RpcRegistry registry = new RpcRegistry(8080);
        registry.start();
    }
}
