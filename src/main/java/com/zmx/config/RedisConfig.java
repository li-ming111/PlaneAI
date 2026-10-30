package com.zmx.config;

import com.alibaba.cloud.ai.dashscope.api.DashScopeAgentApi;
import com.alibaba.cloud.ai.dashscope.api.DashScopeApi;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingModel;
import com.alibaba.cloud.ai.dashscope.embedding.DashScopeEmbeddingOptions;
import org.springframework.ai.document.MetadataMode;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.TokenCountBatchingStrategy;
import org.springframework.ai.vectorstore.redis.RedisVectorStore;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPooled;

@Configuration
public class RedisConfig {

    @Value("${spring.ai.vectorstore.redis.prefix}")
    private String prefix;

    @Value("${spring.ai.vectorstore.redis.index}")
    private String index;

    @Value("${spring.data.redis.port}")
    private String port;

    @Value("${spring.data.redis.host}")
    private String host;

    @Bean
    public JedisPooled jedisPooled(){
        return new JedisPooled(host, Integer.parseInt(port));
    }

    @Bean
    @Qualifier("redisVectorStoreCustom")
    public RedisVectorStore redisVectorStoreCustom(JedisPooled jedisPooled){
        var dashScopeApi = DashScopeApi.builder().apiKey(System.getenv("DASHSCOPE_API_KEY")).build();
        //创建模型
        var embeddingModel = new DashScopeEmbeddingModel(dashScopeApi,
                MetadataMode.EMBED,
                DashScopeEmbeddingOptions.builder()
                        .withModel("text-embedding-v2")
                        .build());
        //调用模型
        return RedisVectorStore.builder(jedisPooled,embeddingModel)
                .indexName(index)
                .prefix( prefix)
                .metadataFields(
                        RedisVectorStore.MetadataField.tag("name"),
                        RedisVectorStore.MetadataField.tag("year")
                )
                .initializeSchema(true)
                .batchingStrategy(new TokenCountBatchingStrategy())
                .build();
    }
}


























