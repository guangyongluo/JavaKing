package com.vilin.canal;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {

  public static final String  REDIS_IP_ADDR = "127.0.0.1";
  public static final String  REDIS_pwd = "123456";
  public static JedisPool jedisPool;

  static {
    JedisPoolConfig jedisPoolConfig=new JedisPoolConfig();
    jedisPoolConfig.setMaxTotal(20);
    jedisPoolConfig.setMaxIdle(10);
    jedisPool=new JedisPool(jedisPoolConfig,REDIS_IP_ADDR,6379,10000,REDIS_pwd);
  }

  public static Jedis getJedis() throws Exception {
    if(null!=jedisPool){
      return jedisPool.getResource();
    }
    throw new Exception("Jedispool is not ok");
  }

}
