package com.alice.rpc.impl;

import com.alice.rpc.annotations.RpcService;
import com.alice.rpc.api.IUser;
import com.alice.rpc.entity.User;

/**
 * @author liuchun
 * @date 2020/02/26  12:14
 */
@RpcService(value = IUser.class)
public class IUserImpl implements IUser {
    @Override
    public User add(User user) {
        System.out.println("add user success");
        return user;
    }

    @Override
    public int del(Integer id) {
        System.out.println("del user success");
        return 1;
    }
}
