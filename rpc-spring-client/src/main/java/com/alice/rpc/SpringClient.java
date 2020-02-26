package com.alice.rpc;

import com.alice.rpc.api.IUser;
import com.alice.rpc.entity.User;

/**
 *
 */
public class SpringClient {
    public static void main(String[] args) {
        RpcProxy proxy = new RpcProxy("127.0.0.1", 8080);
        IUser instance = proxy.newInstance(IUser.class);
        User user = new User();
        user.setName("alice");
        user.setId(1);
        User add = instance.add(user);
        System.out.println(add);
        int del = instance.del(1);
        System.out.println(del);

    }
}
