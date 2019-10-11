package com.xhhp.mmall.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * RedisPool class
 *
 * @author Flc
 * @date 2019/10/7
 */
@Component
public class RedisPool {

    private static JedisPool pool;

    private static Integer maxTotal = 20;    //最大连接数

    private static Integer maxIdle = 8;     //最大空闲数

    private static Integer minIdle = 4;     //最小空闲数

    private static boolean testOnBorrow = true;    //如果为true，每次获得连接时，都会测试连接是否可用

    private static boolean testOnReturn = true;    //如果为true，每次归还连接时，都会测试连接是否可用

    private static String redisIp = "10.213.15.87";  //redis的ip地址

    private static Integer redisPort = 6379;   //redis连接的端口号


    static {
        initialConfig();
    }
    private static void initialConfig() {
        System.out.println(redisIp==null);
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        System.out.println(maxTotal==null);
        jedisPoolConfig.setMaxTotal(maxTotal);
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMinIdle(minIdle);
        jedisPoolConfig.setTestOnBorrow(testOnBorrow);
        jedisPoolConfig.setTestOnReturn(testOnReturn);
        jedisPoolConfig.setBlockWhenExhausted(false);//连接耗尽时，是否阻塞，false会抛出异常，true则会进行阻塞等待，默认值是true

        pool = new JedisPool(jedisPoolConfig, redisIp, redisPort, 1000*2);
    }


    public static Jedis getJedis() {
        return pool.getResource();
    }

    public static void main(String[] args) {
        Jedis jedis = RedisPool.getJedis();
        jedis.set("hello","geely");
        System.out.println(jedis.get("hello"));
        jedis.close();
    }

}
