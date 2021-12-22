package com.example.demo;

import com.example.demo.webSocket.StartWebSocket;
import com.example.demo.webSocket.testSocketServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
		new StartWebSocket().action();
//		new testSocketServer().action();
	}
}
