package com.zmx.controller;

import com.alibaba.cloud.ai.memory.redis.RedisChatMemoryRepository;
import com.zmx.config.BookingConfig;
import com.zmx.domaim.PlaneDto;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.MessageWindowChatMemory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static org.springframework.ai.chat.memory.ChatMemory.CONVERSATION_ID;

@RestController
public class ChatPlaneController {

    //通过构造方法注入
    private final ChatClient chatClient;

    //内存记忆记录数
    private final int MAX_MEMORY_SIZE = 100;

    private final MessageWindowChatMemory messageWindowChatMemory;

    private Resource resource = new ClassPathResource("/data/system-qa.txt");

    @Autowired
    private BookingConfig bookingConfig;

    //构造方法注入
    public ChatPlaneController(ChatClient.Builder builder, RedisChatMemoryRepository redisChatMemoryRepository) {
        messageWindowChatMemory = MessageWindowChatMemory.builder()
                .chatMemoryRepository(redisChatMemoryRepository)
                .maxMessages(MAX_MEMORY_SIZE)
                .build();

        this.chatClient = builder
                //默认系统提示词
                .defaultSystem(resource)
                .defaultAdvisors(
                MessageChatMemoryAdvisor
                        .builder(messageWindowChatMemory)
                        .build()
        ).build();
    }

    @PostMapping(value = "/courseChat",produces = "text/stream;charset=UTF-8")
    public Flux<String> chatPlaneStream(@RequestBody PlaneDto planeDto) {
        return chatClient
                .prompt()
                .user(planeDto.getMessage())
                .system(s->s.param("current_date", LocalDateTime.now()))
                .advisors(p->p.param(CONVERSATION_ID,planeDto.getMemoryId()))
                .tools(bookingConfig)
                .stream()
                .content();
    }
}

















