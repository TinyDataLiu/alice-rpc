package com.alice.rpc.consumer;

import com.alice.rpc.api.IHello;
import com.alice.rpc.api.IUser;
import com.alice.rpc.consumer.proxy.RpcProxy;

/**
 * @author liuchun
 * @date 2020/02/27  14:15
 */
public class RpcConsumer {
    public static void main(String[] args) {
        IHello hello = RpcProxy.create(IHello.class);
        System.out.println(hello.hello("alice"));
        IUser user = RpcProxy.create(IUser.class);
        String res = user.addUser("alice", 18);
        System.out.println(res);
    }
}
