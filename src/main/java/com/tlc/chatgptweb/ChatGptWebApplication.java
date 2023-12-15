package com.tlc.chatgptweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class ChatGptWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(ChatGptWebApplication.class, args);
	}

}
