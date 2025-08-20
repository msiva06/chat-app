package com.example.messaging.websocket.controller;

import com.example.messaging.websocket.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebsocketController {

    @MessageMapping("/chat")
    @SendTo("/topic/chats")
    public String chat(ChatMessage msg) throws  Exception{
        Thread.sleep(1000);
        System.out.println("Connected and the message received is:" + msg.getMessage().toString());
        return msg.getMessage().toString();
    }
}
