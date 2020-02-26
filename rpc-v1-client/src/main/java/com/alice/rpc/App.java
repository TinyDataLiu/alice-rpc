package com.alice.rpc;

import com.alice.rpc.api.HelloService;
import com.alice.rpc.api.IUser;
import com.alice.rpc.client.RemoteProxy;
import com.alice.rpc.entity.User;

/**
 * 调用远程服务
 */
public class App {
    public static void main(String[] args) {
        RemoteProxy<IUser> proxy = new RemoteProxy(8080, "localhost");

        IUser service = proxy.newInstance(IUser.class);

        User user = new User();
        user.setId(1);
        user.setName("alice");

        User add = service.add(user);
        int del = service.del(1);
        System.out.println(add.toString());
        System.out.println(del);
    }
}
