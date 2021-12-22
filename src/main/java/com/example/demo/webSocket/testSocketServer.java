package com.example.demo.webSocket;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * author ShaoCHi
 * Date 2021/12/21 10:12 AM
 * Tongji University
 */

@Controller
@RestController
public class testSocketServer {
  public static void action() {
    //运行端口
    int port = 8888;
    //线程池，定义线程池的大小
    final ExecutorService executorService = Executors.newFixedThreadPool(2 * Runtime.getRuntime().availableProcessors());
    try {
      ServerSocket serverSocket = new ServerSocket(port);
      //服务端的运行
      System.out.println("服务端启动，运行在：" + serverSocket.getLocalSocketAddress());

      while (true) {
        final Socket socket = serverSocket.accept();
        System.out.println("客户端连接，来自：" + socket.getRemoteSocketAddress());
        executorService.execute(new testClientHandler(socket));
      }
    } catch (IOException e) {
      e.printStackTrace();
    } finally {
      executorService.shutdown();
    }
  }
}

