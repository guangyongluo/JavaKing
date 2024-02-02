package com.vilin.lock;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;

public class RedisReentrantLock implements Lock {

  private final StringRedisTemplate stringRedisTemplate;

  private final String lockName;

  private final String lockValue;

  private final Long expireTime;

  public RedisReentrantLock(StringRedisTemplate stringRedisTemplate, String lockName,
      Long expireTime) {
    this.stringRedisTemplate = stringRedisTemplate;
    this.lockName = lockName;
    this.lockValue = UUID.randomUUID() + ":" + Thread.currentThread().getName();
    this.expireTime = expireTime;
  }

  @Override
  public void lock() {
    String script = """
        if redis.call('exists', KEYS[1]) == 0 or redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
            redis.call('hincrby', KEYS[1], ARGV[1], 1)
            redis.call('expire', KEYS[1], ARGV[2])
            return 1
        else
            return 0
        end
        """;

    while(!stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockName),
        lockValue, String.valueOf(expireTime))){
      try {
        TimeUnit.MILLISECONDS.sleep(200);
      } catch (InterruptedException e) {
        throw new RuntimeException(e);
      }
    }

    resetExpire();
  }

  private void resetExpire() {

    String script = """
        if redis.call('hexists', KEYS[1], ARGV[1]) == 1 then
            return redis.call('expire', KEYS[1], ARGV[2])
        else
            return 0
        end
        """;

    new Timer().schedule(new TimerTask() {
      @Override
      public void run() {
        if (stringRedisTemplate.execute(new DefaultRedisScript<>(script, Boolean.class), Arrays.asList(lockName),
            lockValue, String.valueOf(expireTime))) {

          System.out.println("resetExpire() lockName:" + lockName + "\t" + "lockValue:" + lockValue);
          resetExpire();
        }
      }
    }, (this.expireTime * 1000 / 3));
  }


  @Override
  public void lockInterruptibly() throws InterruptedException {

  }

  @Override
  public boolean tryLock() {
    return false;
  }

  @Override
  public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
    return false;
  }

  @Override
  public void unlock() {
    String script = """
        if redis.call('hexists', KEYS[1], ARGV[1]) == 0 then
            return nil
        elseif redis.call('hincrby', KEYS[1], ARGV[1], -1) == 0 then
            return redis.call('del', KEYS[1])
        else
            return 0
        end       
        """;
    final Long result = stringRedisTemplate.execute(new DefaultRedisScript<>(script, Long.class),
        Arrays.asList(lockName),
        lockValue);

    if(result == null) {
      throw new RuntimeException("this lock does not exists.");
    }

  }

  @Override
  public Condition newCondition() {
    return null;
  }
}
