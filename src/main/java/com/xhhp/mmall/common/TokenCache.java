package com.xhhp.mmall.common;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

/**
 * TokenCache class
 *
 * @author Flc
 * @date 2019/9/26
 */
public class TokenCache {

    private static Logger logger = LoggerFactory.getLogger(TokenCache.class);

    //设置缓存的初始化容量、最大容量
    private static LoadingCache<String, String> localCache = CacheBuilder.newBuilder().initialCapacity(1000).maximumSize(10000).expireAfterAccess(12, TimeUnit.HOURS)
            .build(new CacheLoader<String, String>() {
                //默认的数据加载实现，当调用get取值的时候，没有这个key，就会调用这个方法
                @Override
                public String load(String s) throws Exception {
                    return "null";
                }
            });

    public static void set(String key, String value) {
        localCache.put(key, value);
    }

    public static String get(String key) {
        try {
            String answer = localCache.get(key);
            if(answer.equals("null")) {
                return null;
            }
            return answer;
        } catch (Exception e) {
            logger.error("localCache get error" ,e);
        }
        return null;
    }


}

