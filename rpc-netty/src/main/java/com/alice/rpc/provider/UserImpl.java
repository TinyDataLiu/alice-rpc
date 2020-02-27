package com.alice.rpc.provider;

import com.alice.rpc.api.IUser;

/**
 * @author liuchun
 * @date 2020/02/27  13:06
 */
public class UserImpl implements IUser {
    @Override
    public String addUser(String name, Integer age) {
        System.out.println(String.format("name=%s,age=%s", name, age));
        return name + "--->" + age;
    }
}
