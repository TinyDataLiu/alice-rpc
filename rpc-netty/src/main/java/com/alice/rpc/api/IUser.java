package com.alice.rpc.api;

/**
 * @author liuchun
 * @date 2020/02/27  13:02
 */
public interface IUser {
    /**
     * 用户添加
     *
     * @param name
     * @param age
     * @return
     */
    String addUser(String name, Integer age);
}
