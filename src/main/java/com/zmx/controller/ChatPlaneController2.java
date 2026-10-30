package com.zmx.controller;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.HashMap;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatPlaneController2 {

    //通过构造方法注入
    private final ChatClient chatClient;

    //内存记忆记录数
    private final int MAX_MEMORY_SIZE = 100;

    private final MessageWindowChatMemory messageWindowChatMemory;

    @Value("classpath:/data/system-qa.txt")
    private Resource resource ;

    //构造方法注入
    public ChatPlaneController2(ChatClient.Builder builder, RedisChatMemoryRepository redisChatMemoryRepository) {
        messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(MAX_MEMORY_SIZE)
                .build();

        this.chatClient = builder
                .defaultAdvisors(
                MessageChatMemoryAdvisor
                        .builder(messageWindowChatMemory)
                        .build()
        ).build();
    }

    @GetMapping(value = "chatPlaneStream2")
    public String chatPlaneStream2(String message,String userId) {
        //提示词模板
        PromptTemplate promptTemplate = new PromptTemplate(resource);
        //使用map完成提示词中的参数设置
        HashMap<String, Object> map = new HashMap<>();
        map.put("current_date", LocalDateTime.now());

        return chatClient
                .prompt(promptTemplate.create(map))
                .user(message)
                .advisors(p->p.param(CONVERSATION_ID,userId))
                .call().content();
    }
}

















