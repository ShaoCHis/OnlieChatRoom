package com.example.demo.webSocket;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Controller
@RestController
public class StartWebSocket {

//    @GetMapping(value = "/action/webSocket")
    public static void action(){
        //netty通信通过io线程EventLoop进行管理
        //EventLoopGroup 是一组 EventLoop 的抽象，一个 EventLoopGroup 当中会包含一个或多个 EventLoop，
        //EventLoopGroup 提供 next 接口，可以从一组 EventLoop 里面按照一定规则获取其中一个 EventLoop 来处理任务。

        //监控TCP连接
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        //监控IO操作
        EventLoopGroup workGroup = new NioEventLoopGroup();

        try {
            //开启服务端
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(eventLoopGroup,workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new MyWebSocketChannelHandler());
            System.out.println("服务端开启等待客户端连接..");
            Channel channel = serverBootstrap.bind(8888).sync().channel();
            channel.closeFuture().sync();
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            //退出程序
            eventLoopGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }
}
