package com.alice.rpc.api;

import com.alice.rpc.entity.User;

/**
 * @author liuchun
 * @date 2020/02/26  12:12
 */
public interface IUser {
    User add(User user);

    int del(Integer id);
}
