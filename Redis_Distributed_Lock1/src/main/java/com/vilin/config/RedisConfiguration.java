package com.vilin.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfiguration {

  @Bean
  public RedisTemplate redisTemplate(RedisConnectionFactory factory) {
    RedisTemplate<Object, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(factory);
    //使用jackson进行序列化
    Jackson2JsonRedisSerializer jsonRedisSerializer =
        new Jackson2JsonRedisSerializer(Object.class);
    //规定序列化规则
    ObjectMapper objectMapper = new ObjectMapper();
    /**
     * 第一个参数指的是序列化的域，ALL指的是字段、get和set方法、构造方法
     * 第二个参数指的是序列化哪些访问修饰符，默认是public，ANY指任何访问修饰符
     */
    objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
    //指定序列化输入的类型，类必须是非final修饰的类
    objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
    jsonRedisSerializer.setObjectMapper(objectMapper);
    //序列化key value
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(jsonRedisSerializer);
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(jsonRedisSerializer);
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }

  @Bean
  public RedissonClient redissonClient(){
    Config config = new Config();
    config.useSingleServer()
        .setAddress("redis://127.0.0.1:6379")
        .setDatabase(0)
        .setPassword("123456");
    return Redisson.create(config);
  }
}
