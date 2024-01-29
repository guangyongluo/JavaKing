package com.vilin.bloomfilter;

import static com.vilin.contants.RedisConstants.BLOOM_FILTER;
import static com.vilin.contants.RedisConstants.CUSTOMER_PREFIX;

import com.vilin.bitmap.service.CustomerService;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BloomFilterInitializer {

  @Resource
  private RedisTemplate redisTemplate;

  @Resource
  private CustomerService customerService;

  @PostConstruct
  public void init(){
    final List<Integer> allCustomerIds = customerService.getAllCustomerIds();
    final List<String> whiteList = allCustomerIds.stream().map(id -> CUSTOMER_PREFIX + id)
        .collect(Collectors.toList());
    whiteList.forEach(key -> {
      long hashValue = Math.abs(key.hashCode());
      final long index = (long) (hashValue % Math.pow(2, 32));
      redisTemplate.opsForValue().setBit(BLOOM_FILTER, index, true);
      log.info("load key {} for bloom filter in index : {}", key, index);
    });
  }
}
