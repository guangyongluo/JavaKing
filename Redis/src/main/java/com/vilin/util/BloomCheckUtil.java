package com.vilin.util;

import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BloomCheckUtil {

  @Resource
  private RedisTemplate redisTemplate;

  public Boolean checkWhiteList(String key, String bloomFilterKey){
    final long hashValue = Math.abs(key.hashCode());
    final long index = (long)(hashValue % Math.pow(2, 32));
    final Boolean result = redisTemplate.opsForValue().getBit(bloomFilterKey, index);

    log.info("the key {} exists or not : {}", key, result);
    return result;
  }
}
