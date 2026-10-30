package com.zmx.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.InMemoryChatMemoryRepository;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatPlaneController1 {

    //通过构造方法注入
    private final ChatClient chatClient;

    //内存记忆
    private final InMemoryChatMemoryRepository memoryRepository = new InMemoryChatMemoryRepository();
    private final int MAX_MEMORY_SIZE = 100;
    //信息窗口
    private final MessageWindowChatMemory messageWindowChatMemory = MessageWindowChatMemory
            .builder()
            .chatMemoryRepository(memoryRepository)
            .maxMessages(MAX_MEMORY_SIZE)
            .build();

    //构造方法注入
    public ChatPlaneController1(ChatClient.Builder builder) {
        this.chatClient = builder.defaultAdvisors(
                MessageChatMemoryAdvisor.builder(messageWindowChatMemory)
                        .build()
        ).build();
    }

    //聊天
    @GetMapping("/chatPlane1")
    public String chatPalne1(String message,String userId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(p->p.param(CONVERSATION_ID,userId))
                .call()
                .content();
    }

    @GetMapping(value = "chatPlaneStrem1",produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chatPlaneStrem1(String message,String userId) {
        return chatClient
                .prompt()
                .user(message)
                .advisors(p->p.param(CONVERSATION_ID,userId))
                .stream()
                .content();
    }
}

















