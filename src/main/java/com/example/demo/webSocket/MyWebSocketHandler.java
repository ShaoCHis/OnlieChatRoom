package com.example.demo.webSocket;

import com.example.demo.config.NettyConfig;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 接受/处理/响应客户端websocket请求的核心业务处理类
 */
public class MyWebSocketHandler extends SimpleChannelInboundHandler<Object> {

  private static final Map<String, ChannelHandlerContext> socketMap = new ConcurrentHashMap<>();
  private static final String WEB_SOCKET_URL = "ws://localhost:8888/webSocket";
  private WebSocketServerHandshaker webSocketServerHandshaker;
  //与每个线程的client对应
  private String name;
  private ChannelHandlerContext client;

  //客户端与服务端创建链接的时候调用
  @Override
  public void channelActive(ChannelHandlerContext context) throws Exception {
    this.client = context;
    NettyConfig.group.add(context.channel());
    System.out.println("客户端与服务端连接开启");
  }

  //客户端与服务端断开连接的时候调用
  @Override
  public void channelInactive(ChannelHandlerContext context) throws Exception {
    NettyConfig.group.remove(context.channel());
    System.out.println("客户端与服务端连接断开");
  }

  //服务端接收客户端发送过来的数据结束之后调用
  @Override
  public void channelReadComplete(ChannelHandlerContext context) throws Exception {
    context.flush();
  }

  //工程出现异常的时候调用
  @Override
  public void exceptionCaught(ChannelHandlerContext context, Throwable throwable) throws Exception {
    throwable.printStackTrace();
    context.close();
  }

  //服务端处理客户端websocket请求的核心方法
  @Override
  protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {
    //处理客户端向服务端发起的http握手请求
    if (o instanceof FullHttpRequest) {
      handHttpRequest(channelHandlerContext, (FullHttpRequest) o);
    } else if (o instanceof WebSocketFrame) {
      //处理websocket链接业务
      handWebSocketFrame(channelHandlerContext, (WebSocketFrame) o);
    }
  }

  /**
   * 处理客户端与服务端之间的websocket业务
   *
   * @param context
   * @param webSocketFrame
   */
  private void handWebSocketFrame(ChannelHandlerContext context, WebSocketFrame webSocketFrame) {
    if (webSocketFrame instanceof CloseWebSocketFrame) {//判断是否是关闭websocket的指令
      webSocketServerHandshaker.close(context.channel(), (CloseWebSocketFrame) webSocketFrame.retain());
    }
    if (webSocketFrame instanceof PingWebSocketFrame) {//判断是否是ping消息
      context.channel().write(new PongWebSocketFrame(webSocketFrame.content().retain()));
      return;
    }
    if (webSocketFrame instanceof BinaryWebSocketFrame) {//判断是否是二进制消息
      System.out.println(webSocketFrame);
      ByteBuf byteBuf = Unpooled.directBuffer(webSocketFrame.content().capacity());
      byteBuf.writeBytes(webSocketFrame.content());
      groupSendPicture(new BinaryWebSocketFrame(byteBuf));
      groupChat("(群发)"+this.name+"上传了一张图片，请在聊天框左侧查看！");
      return;
    }
    //返回应答消息
    //获取客户端向服务端发送的消息
    String request = ((TextWebSocketFrame) webSocketFrame).text();
    System.out.println("服务端收到客户端的消息：" + request);
    //注册
    if (request.startsWith("register:")) {
      String[] segments = request.split(":");
      if (segments.length == 2 && segments[0].equals("register")) {
        String name = segments[1];
        register(name);
      }
    }
    //群聊
    if (request.startsWith("groupChat:")) {
      String[] segments = request.split(":");
      if (segments.length == 2 && segments[0].equals("groupChat")) {
        String message = segments[1];
        groupChat(message);
      }
    }
    //私聊
    if (request.startsWith("privateChat:")) {
      String[] segments = request.split(":");
      if (segments.length == 3 && segments[0].equals("privateChat")) {
        String name = segments[1];
        String message = segments[2];
        privateChat(name, message);
      }
    }
    //转发
    if(request.startsWith("transmit:")){
      String[] segments=request.split(":");
      if(segments.length==4&&segments[0].equals("transmit")){
        String name=segments[1];
        String message=segments[2]+":"+segments[3];
        transmitMessage(name,message);
      }
    }
    //退出
    if (request.equalsIgnoreCase("bye")) {
      quitChat();
    }
  }


  /**
   * 处理客户端向服务端发起http握手请求业务
   *
   * @param context
   * @param fullHttpRequest
   */
  private void handHttpRequest(ChannelHandlerContext context, FullHttpRequest fullHttpRequest) {
    if (!fullHttpRequest.getDecoderResult().isSuccess() || !("websocket".equals(fullHttpRequest.headers().get("Upgrade")))) {//判断是否http握手请求
      sendHttpResponse(context, fullHttpRequest, new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
      return;
    }
    WebSocketServerHandshakerFactory webSocketServerHandshakerFactory = new WebSocketServerHandshakerFactory(WEB_SOCKET_URL, null, false);
    webSocketServerHandshaker = webSocketServerHandshakerFactory.newHandshaker(fullHttpRequest);
    if (webSocketServerHandshaker == null) {
      WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
    } else {
      webSocketServerHandshaker.handshake(context.channel(), fullHttpRequest);
    }
  }

  /**
   * 服务端想客户端发送响应消息
   *
   * @param context
   * @param fullHttpRequest
   * @param defaultFullHttpResponse
   */
  private void sendHttpResponse(ChannelHandlerContext context, FullHttpRequest fullHttpRequest, DefaultFullHttpResponse defaultFullHttpResponse) {
    if (defaultFullHttpResponse.getStatus().code() != 200) {
      ByteBuf buf = Unpooled.copiedBuffer(defaultFullHttpResponse.getStatus().toString(), CharsetUtil.UTF_8);
      defaultFullHttpResponse.content().writeBytes(buf);
      buf.release();
    }
    //服务端向客户端发送数据
    ChannelFuture future = context.channel().writeAndFlush(defaultFullHttpResponse);
    if (defaultFullHttpResponse.getStatus().code() != 200) {
      future.addListener(ChannelFutureListener.CLOSE);
    }

  }


  //下线
  private void quitChat() {
    socketMap.remove(this.name);
    groupChatLogout("(群聊)" + this.name + "下线了");
    printOnlineClient();
  }

  //消息的转发
  private void transmitMessage(String name,String message){
    ChannelHandlerContext socket = socketMap.get(name);
    if (socket != null) {
      //在接收端显示发送对象
      sendMessage(socket, "(转发From"+this.name+")"+message);
    }else {
      sendMessage(this.client, "(系统)好友未上线噢，请稍后再试！");
    }
  }

  //通过name与对应客户端进行通信
  private void privateChat(String name, String message) {
    ChannelHandlerContext socket = socketMap.get(name);
    if (socket != null) {
      //在接收端显示发送对象
      sendMessage(socket, "(私聊)" + this.name + " 说:" + message);
      //在发送端显示目的对象
      sendMessage(this.client, "（私聊To" + name + ")" + ":" + message);
    }else {
      sendMessage(this.client, "(系统)好友未上线噢，请稍后再试！");
    }
  }

  //群聊
  private void groupChat(String message) {
    //通过遍历对每个客户端进行消息的发送
    for (Map.Entry<String, ChannelHandlerContext> entry : socketMap.entrySet()) {
      ChannelHandlerContext socket = entry.getValue();
      sendMessage(socket, "(群发)" + this.name + "说：" + message);
    }
  }

  //群发图片
  private void groupSendPicture(BinaryWebSocketFrame binaryWebSocketFrame) {
    //通过遍历对每个客户端进行消息的发送
    for (Map.Entry<String, ChannelHandlerContext> entry : socketMap.entrySet()) {
      ChannelHandlerContext socket = entry.getValue();
      socket.channel().writeAndFlush(binaryWebSocketFrame);
    }
  }

  //登陆广播
  private void groupChatLogin(String message) {
    //登陆成功时，显示当前在线人数
    sendMessage(this.client,"当前在线的客户端有：" + socketMap.size() + "个，名称列表如下：");
    for (String name : socketMap.keySet()) {//keySet()取得所有的key信息
      sendMessage(this.client,name);
    }
    //通过遍历对每个客户端进行消息的发送
    for (Map.Entry<String, ChannelHandlerContext> entry : socketMap.entrySet()) {
      ChannelHandlerContext socket = entry.getValue();
      sendMessage(socket, message);
    }
  }

  //退出广播
  private void groupChatLogout(String message) {
    //通过遍历对每个客户端进行消息的发送
    for (Map.Entry<String, ChannelHandlerContext> entry : socketMap.entrySet()) {
      ChannelHandlerContext socket = entry.getValue();
      sendMessage(socket, message);
    }
  }

  //登陆
  private void register(String name) {
    this.name = name;
    socketMap.put(name, this.client);
    System.out.println(name + "登陆到系统中");
    groupChatLogin("(群发)欢迎，" + name + "登陆成功");
    printOnlineClient();
  }

  //打印当前在线客户人数
  private void printOnlineClient() {
    System.out.println("当前在线的客户端有：" + socketMap.size() + "个，名称列表如下：");
    for (String name : socketMap.keySet()) {//keySet()取得所有的key信息
      System.out.println(name);
    }
  }

  //发指定消息到客户端
  private void sendMessage(ChannelHandlerContext client, String message) {
    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(message);
    client.channel().writeAndFlush(textWebSocketFrame);
  }
}
