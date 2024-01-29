package com.vilin.hyperloglog.service;

import static com.vilin.contants.RedisConstants.HYPERLOGLOG_KEY;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class HyperLogLogService {

  @Resource
  private RedisTemplate redisTemplate;

  @PostConstruct
  public void init() {
    new Thread(() -> {
      Random random = new Random(67);

      for (int i = 0; i < 200; i++) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(random.nextInt(256)).append(".");
        stringBuilder.append(random.nextInt(256)).append(".");
        stringBuilder.append(random.nextInt(256)).append(".");
        stringBuilder.append(random.nextInt(256));

        try {
          TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
          throw new RuntimeException(e);
        }

        redisTemplate.opsForHyperLogLog().add(HYPERLOGLOG_KEY, stringBuilder.toString());

        log.info("IP {} access web size.", stringBuilder.toString());
      }
    }, "T1").start();
  }

  public Long getPageUV(){
    return redisTemplate.opsForHyperLogLog().size(HYPERLOGLOG_KEY);
  }


}
