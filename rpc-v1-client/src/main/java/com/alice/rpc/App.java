package com.alice.rpc;

import com.alice.rpc.api.HelloService;
import com.alice.rpc.client.RemoteProxy;
import com.alice.rpc.entity.User;

/**
 * 调用远程服务
 */
public class App {
    public static void main(String[] args) {
        RemoteProxy<HelloService> proxy = new RemoteProxy(8080, "localhost");
        HelloService helloService = proxy.newInstance(HelloService.class);
        String alice = helloService.sayHello("alice");

        User user = new User();
        user.setId(1);
        user.setName("alice");

        String message = helloService.addUser(user);

        System.out.println(message);
        System.out.println(alice);
    }
}
