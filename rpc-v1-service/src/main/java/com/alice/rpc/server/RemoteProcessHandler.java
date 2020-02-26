package com.alice.rpc.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 提供远程服务
 *
 * @author liuchun
 * @date 2020/02/26  9:24
 */
public class RemoteProcessHandler {

    private int port;

    private ExecutorService pool = Executors.newCachedThreadPool();

    public RemoteProcessHandler(int port) {
        this.port = port;
    }

    /**
     * 启动服务
     */
    public void process() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            /**
             * 自循环监听请求
             */
            while (true) {
                // 监听请求
                System.out.println("server start success");
                Socket socket = serverSocket.accept();
                // 通过线程池来接收请求，控制并发数，避免IO阻塞
                pool.execute(new ServerProcess(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
