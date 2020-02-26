package com.alice.rpc;

import com.alice.rpc.server.RemoteProcessHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.ServerSocket;

/**
 * 服务启动类
 */
public class ServerV1 {

    public static void main(String[] args) {
        // 启动服务
        RemoteProcessHandler handler = new RemoteProcessHandler(8080);
        handler.process();
    }

}
