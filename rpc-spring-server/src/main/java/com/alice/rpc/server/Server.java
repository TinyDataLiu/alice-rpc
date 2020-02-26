package com.alice.rpc.server;

import com.alice.rpc.annotations.RpcService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author liuchun
 * @date 2020/02/26  11:43
 */
public class Server implements ApplicationContextAware, InitializingBean {

    private int port;
    private ExecutorService pool = new ThreadPoolExecutor(1, 5, 60L, TimeUnit.SECONDS, new LinkedBlockingDeque<>());
    private Map<String, Object> serversMap = new HashMap<>();

    public Server(int port) {
        this.port = port;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(RpcService.class);
        for (Object o : beans.values()) {
            RpcService annotation = o.getClass().getAnnotation(RpcService.class);
            // 将提供远程服务的类统一管理起来
            String serviceName = annotation.value().getName();
            System.out.println("service add success " + serviceName);
            serversMap.put(serviceName, o);
        }
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("service start....");
            while (true) {
                // 监听请求
                Socket socket = serverSocket.accept();
                pool.execute(new RpcInvoke(serversMap, socket));
            }
        }
    }
}
