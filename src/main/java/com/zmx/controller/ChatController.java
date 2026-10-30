package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    //通过构造方法注入
    private final ChatModel chatModel;

    public ChatController(ChatModel chatModel) {
        this.chatModel = chatModel;
    }

    //聊天
    @GetMapping("/chat")
    public String chat(String question) {
        return ChatClient.create(chatModel)
                .prompt()
                .user(question)
                .call()
                .content();
    }
}

















