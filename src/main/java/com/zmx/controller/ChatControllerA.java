/*
package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.deepseek.DeepSeekChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatControllerA {

    //通过构造方法注入
    private final ChatClient chatClient;

    public ChatControllerA(ChatModel chatModel) {
        this.chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(MessageWindowChatMemory.builder().build())
                        .build())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .defaultOptions(DeepSeekChatOptions.builder().temperature(0.7d).build())
                .build();
    }

    //聊天
    @GetMapping("/chatA")
    public String chat(String question,String memoryId) {
        return chatClient
                .prompt()
                .user(question)
                .advisors()
                .call()
                .content();
    }
}

















*/
