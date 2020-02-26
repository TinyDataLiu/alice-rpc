package com.alice.rpc.server;

import com.alice.rpc.api.HelloService;
import com.alice.rpc.entity.User;

/**
 * @author liuchun
 * @date 2020/02/26  9:40
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "Hello " + name;
    }

    @Override
    public String addUser(User user) {
        // 模拟数据入库
        System.out.println("user add success");
        return "user add success";
    }
}
