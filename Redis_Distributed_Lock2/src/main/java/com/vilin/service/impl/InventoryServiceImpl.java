package com.vilin.service.impl;

import com.vilin.lock.RedisReentrantLock;
import com.vilin.service.InventoryService;
import java.util.Arrays;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class InventoryServiceImpl implements InventoryService {

  private final static String INVENTORY_KEY = "inventory_laptop";

  private final static String DISTRIBUTED_LOCK = "distributed_lock";

  @Value("${server.port}")
  private String port;

  @Resource
  private StringRedisTemplate stringRedisTemplate;

  // JVM锁是不符合分布式要求的，使用nginx反向代理加上ApiFox压测后出现超卖的情况
  @Override
  public String reduceInventory() {
    String message = "";

    ReentrantLock lock = new ReentrantLock();

    lock.lock();

    try{
      final String inventory = stringRedisTemplate.opsForValue().get(INVENTORY_KEY);
      int num = inventory == null ? 0 : Integer.parseInt(inventory);

      stringRedisTemplate.opsForValue().set(INVENTORY_KEY, String.valueOf(--num));

      message = "sales number " + num + " product by server " + port +"." ;

      log.info(message);

    }catch (Exception e) {
      message = "parse inventory number failed";
      log.info(message);
      return message;
    } finally {
      lock.unlock();
    }

    return message;
  }

  @Override
  public String reduceInventoryWithDistributedLock() {
    String message = "";

    String lockValue = UUID.randomUUID() + ":" + Thread.currentThread().getName();

    while(!stringRedisTemplate.opsForValue().setIfAbsent(DISTRIBUTED_LOCK, lockValue, 10L, TimeUnit.SECONDS)){
      try {
        TimeUnit.MICROSECONDS.sleep(20L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    try{
      final String inventory = stringRedisTemplate.opsForValue().get(INVENTORY_KEY);
      int num = inventory == null ? 0 : Integer.parseInt(inventory);

      stringRedisTemplate.opsForValue().set(INVENTORY_KEY, String.valueOf(--num));

      message = "sales number " + num + " product by server " + port +"." ;

      log.info(message);

    }catch (Exception e) {
      message = "parse inventory number failed";
      log.info(message);
      return message;
    } finally {
      stringRedisTemplate.delete(DISTRIBUTED_LOCK);
    }

    return message;
  }

  // V2 在删除锁时，判断是否是自己所加的锁，只能删除自己加的锁
  @Override
  public String reduceInventoryWithMutexLock() {
    String message = "";

    String lockValue = UUID.randomUUID() + ":" + Thread.currentThread().getName();

    while(!stringRedisTemplate.opsForValue().setIfAbsent(DISTRIBUTED_LOCK, lockValue, 10L, TimeUnit.SECONDS)){
      try {
        TimeUnit.MICROSECONDS.sleep(20L);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    try{
      final String inventory = stringRedisTemplate.opsForValue().get(INVENTORY_KEY);
      int num = inventory == null ? 0 : Integer.parseInt(inventory);

      stringRedisTemplate.opsForValue().set(INVENTORY_KEY, String.valueOf(--num));

      message = "sales number " + num + " product by server " + port +"." ;

      log.info(message);

    }catch (Exception e) {
      message = "parse inventory number failed";
      log.info(message);
      return message;
    } finally {
      // 这样的改进还是会存在问题，判断和删除不是原子操作，这样有可能在判断成功后被其他系统事务打断导致删除不彻底
      // 这时可以考虑使用Lua脚本来编写脚本，把判断和删除两个Redis命令通过Lua脚本的运行在Redis server上保证两
      // 条命令的原子性
//      if(stringRedisTemplate.opsForValue().get(INVENTORY_KEY).equals(lockValue)) {
//        stringRedisTemplate.delete(DISTRIBUTED_LOCK);

      String luaScript = "if redis.call('get',KEYS[1]) == ARGV[1] then return redis.call('del',KEYS[1]) else return 0 end";
      stringRedisTemplate.execute(new DefaultRedisScript<>(luaScript, Boolean.class),
          Arrays.asList(DISTRIBUTED_LOCK), lockValue);
    }

    return message;
  }

  @Override
  public String reduceInventoryWithReentrantLock() {
    String message = "";

    RedisReentrantLock lock = new RedisReentrantLock(stringRedisTemplate, DISTRIBUTED_LOCK, 30L);

    lock.lock();

    try{
      final String inventory = stringRedisTemplate.opsForValue().get(INVENTORY_KEY);
      int num = inventory == null ? 0 : Integer.parseInt(inventory);

      stringRedisTemplate.opsForValue().set(INVENTORY_KEY, String.valueOf(--num));

      message = "sales number " + num + " product by server " + port +"." ;

      log.info(message);

    }catch (Exception e) {
      message = "parse inventory number failed";
      log.info(message);
      return message;
    } finally {
      lock.unlock();
    }

    return message;
  }
}
