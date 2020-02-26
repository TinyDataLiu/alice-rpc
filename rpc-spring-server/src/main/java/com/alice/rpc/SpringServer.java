package com.alice.rpc;

import com.alice.rpc.server.Server;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 *
 */
@Configuration
@ComponentScan
public class SpringServer {


    @Bean
    public Server server() {
        return new Server(8080);
    }

    /**
     * 发布服务
     *
     * @param args
     */
    public static void main(String[] args) {
        //服务发布
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SpringServer.class);
        context.start();
    }
}
