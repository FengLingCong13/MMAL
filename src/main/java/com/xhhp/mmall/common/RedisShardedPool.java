package com.xhhp.mmall.common;

import org.springframework.stereotype.Component;
import redis.clients.jedis.*;
import redis.clients.jedis.util.Hashing;
import redis.clients.jedis.util.Sharded;

import java.util.ArrayList;
import java.util.List;

/**
 * RedisShardedPool class
 *
 * @author Flc
 * @date 2019/10/14
 */
@Component
public class RedisShardedPool {
    private static ShardedJedisPool pool;   //shared jedisPool

    private static Integer maxTotal = 20;    //最大连接数

    private static Integer maxIdle = 8;     //最大空闲数

    private static Integer minIdle = 4;     //最小空闲数

    private static boolean testOnBorrow = true;    //如果为true，每次获得连接时，都会测试连接是否可用

    private static boolean testOnReturn = true;    //如果为true，每次归还连接时，都会测试连接是否可用

    private static String redis1Ip = "10.213.15.87";  //redis的ip地址

    private static Integer redis1Port = 6379;   //redis连接的端口号

    private static String redis2Ip = "10.213.15.87";  //redis的ip地址

    private static Integer redis2Port = 6380;   //redis连接的端口号


    static {
        initialConfig();
    }
    private static void initialConfig() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        System.out.println(maxTotal==null);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(false);//连接耗尽时，是否阻塞，false会抛出异常，true则会进行阻塞等待，默认值是true

        JedisShardInfo info1 = new JedisShardInfo(redis1Ip,redis1Port,1000*2);
        JedisShardInfo info2 = new JedisShardInfo(redis2Ip,redis2Port,1000*2);

        List<JedisShardInfo> jedisShardInfoList = new ArrayList<JedisShardInfo>(2);

        jedisShardInfoList.add(info1);
        jedisShardInfoList.add(info2);

        pool = new ShardedJedisPool(jedisPoolConfig,jedisShardInfoList, Hashing.MURMUR_HASH, Sharded.DEFAULT_KEY_TAG_PATTERN);
    }


    public static ShardedJedis getJedis() {
        return pool.getResource();
    }

    public static void main(String[] args) {
        ShardedJedis jedis = pool.getResource();

        for(int i = 0; i < 10; i++) {
            jedis.set("key" + i, "value" + i);
        }
        jedis.close();
    }
}
