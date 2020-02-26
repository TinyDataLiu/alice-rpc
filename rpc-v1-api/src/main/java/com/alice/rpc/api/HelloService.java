package com.alice.rpc.api;

import com.alice.rpc.entity.User;

/**
 * 先开始第一步，我们先编写一个提供调用的服务。
 *
 * @author liuchun
 * @date 2020/02/26  9:19
 */
public interface HelloService {

    String sayHello(String name);

    String addUser(User user);

}
