package com.zmx.controller;

import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
public class RedisController {

    private final RedisVectorStore redisVectorStore;

    @Autowired
    public RedisController(@Qualifier("redisVectorStoreCustom") RedisVectorStore redisVectorStore) {
        this.redisVectorStore = redisVectorStore;
    }

    // 添加向量
    @GetMapping("/addVector")
    public void addVector() {
        Document document = new Document("这是内容");
        redisVectorStore.add(List.of(document));
    }

    //添加向量数据
    @PostMapping("/writeRedis")
    public String writeRedis(MultipartFile file)  throws Exception {
        List<Document> documents = new TikaDocumentReader(new InputStreamResource(file.getInputStream())).read();
        Stream<String[]> stream = documents.stream()
                .map(Document::getFormattedContent)
                .map(res -> res.split("\n"));
        //去掉空格
        List<String> list = stream.flatMap(Stream::of)
                .filter(res -> res.contains("准点率"))
                .collect(Collectors.toList());
        //转为文档
        List<Document> documentList = list.stream()
                .map(doc -> new Document(doc))
                .collect(Collectors.toList());
        //向量存储
        documentList.forEach(document -> redisVectorStore.add(List.of(document)));
        return "添加成功";
    }

    //搜索
    @GetMapping("/search")
    public List<Document> search(@RequestParam String query) {
        return redisVectorStore.similaritySearch(
                SearchRequest
                        .builder()
                        .query(query)
                        .topK(5)
                        .build()
        );
    }

}



























