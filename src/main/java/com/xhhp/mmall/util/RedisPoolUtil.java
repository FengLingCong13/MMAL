package com.xhhp.mmall.util;

import com.xhhp.mmall.common.RedisPool;
import lombok.extern.slf4j.Slf4j;
import redis.clients.jedis.Jedis;

/**
 * RedisPoolUtil class
 *
 * @author Flc
 * @date 2019/10/8
 */
@Slf4j
public class RedisPoolUtil {

    public static String set(String key, String value) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.set(key, value);
        }catch (Exception e) {
            log.error("set key:{},value:{} error", key, value, e);
            jedis.close();
        }
        return result;
    }

    public static String get(String key) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.get(key);
        }catch (Exception e) {
            log.error("set key:{},error", key, e);
            jedis.close();
        }
        return result;
    }

    public static String setEx(String key, String value, int exTime) {
        Jedis jedis = null;
        String result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.setex(key, exTime, value);
        } catch (Exception e) {
            log.error("setEx error key:{},exTime:{},value:{}",key,exTime,value,e);
        }
        return result;
    }

    /**
     * 设置键值对的有效时间,单位是秒
     * @param key
     * @param exTime
     * @return
     */
    public static Long expire(String key,int exTime) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.expire(key, exTime);
        } catch (Exception e) {
            log.error("expire error key:{},exTime:{}",key,exTime,e);
        }
        return result;
    }

    public static Long del(String key) {
        Jedis jedis = null;
        Long result = null;
        try {
            jedis = RedisPool.getJedis();
            result = jedis.del(key);
        }catch (Exception e) {
            log.error("del key:{},error", key, e);
            jedis.close();
        }
        return result;
    }

    public static void main(String[] args) {
        RedisPoolUtil.set("ee","bb");
        RedisPoolUtil.setEx("ee","qq",30*20);
        RedisPoolUtil.expire("ee",30*60);
        RedisPoolUtil.del("ee");
    }


}
