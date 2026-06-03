package com.htweb.configs;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.htweb.core.helpers.cache.SimpleGrantedAuthorityMixin;
import com.htweb.core.subscribers.JobPostSubscriber;
import com.htweb.core.subscribers.NotificationSubscriber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@PropertySource(value = "classpath:config.properties", ignoreResourceNotFound = false)
public class RedisConfig {

    @Autowired
    private Environment env;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(env.getRequiredProperty("redis.host"));
        config.setPort(env.getRequiredProperty("redis.port", Integer.class));
        config.setPassword(env.getRequiredProperty("redis.password"));
        return new LettuceConnectionFactory(config);
    }

    @Bean("redisObjectMapper")
    public ObjectMapper redisObjectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        mapper.activateDefaultTyping(
                BasicPolymorphicTypeValidator.builder()
                        .allowIfBaseType(Object.class)
                        .build(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );
        mapper.addMixIn(SimpleGrantedAuthority.class, SimpleGrantedAuthorityMixin.class);
        return mapper;
    }

    @Bean
    public GenericJackson2JsonRedisSerializer redisSerializer(
            @Qualifier("redisObjectMapper") ObjectMapper redisObjectMapper) {
        return new GenericJackson2JsonRedisSerializer(redisObjectMapper);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
            LettuceConnectionFactory redisConnectionFactory,
            GenericJackson2JsonRedisSerializer redisSerializer) {

        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        template.setKeySerializer(new StringRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);

        return template;
    }

    @Bean
    public CacheManager cacheManager(
            LettuceConnectionFactory redisConnectionFactory,
            GenericJackson2JsonRedisSerializer redisSerializer) {

        RedisCacheConfiguration defaultConfig = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(30))
                .serializeKeysWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair
                        .fromSerializer(redisSerializer))
                .disableCachingNullValues();

        Map<String, RedisCacheConfiguration> cacheConfigs = new HashMap<>();
        cacheConfigs.put("rolePermissionCache", defaultConfig.entryTtl(Duration.ofMinutes(15)));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConfig)
                .withInitialCacheConfigurations(cacheConfigs)
                .build();
    }

    @Bean
    public ChannelTopic notificationTopic() {
        return new ChannelTopic("system_notifications");
    }

    @Bean
    public ChannelTopic jobPostTopic() {
        return new ChannelTopic("job_post");
    }

    @Bean
    public MessageListenerAdapter notificationListenerAdapter(
            NotificationSubscriber subscriber,
            GenericJackson2JsonRedisSerializer redisSerializer) {

        MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber, "onMessage");
        adapter.setSerializer(redisSerializer);
        return adapter;
    }

    @Bean
    public MessageListenerAdapter jobPostListenerAdapter(
            JobPostSubscriber subscriber,
            GenericJackson2JsonRedisSerializer redisSerializer) {

        MessageListenerAdapter adapter = new MessageListenerAdapter(subscriber, "onMessage");
        adapter.setSerializer(redisSerializer);
        return adapter;
    }

    @Bean
    public RedisMessageListenerContainer redisContainer(
            LettuceConnectionFactory redisConnectionFactory,
            @Qualifier("notificationListenerAdapter") MessageListenerAdapter notificationListenerAdapter,
            @Qualifier("notificationTopic") ChannelTopic notificationTopic,
            @Qualifier("jobPostListenerAdapter") MessageListenerAdapter jobPostListenerAdapter,
            @Qualifier("jobPostTopic") ChannelTopic jobPostTopic) {

        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(redisConnectionFactory);

        container.addMessageListener(notificationListenerAdapter, notificationTopic);
        container.addMessageListener(jobPostListenerAdapter, jobPostTopic);
        return container;
    }
}