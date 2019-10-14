package com.xhhp.mmall.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.web.http.DefaultCookieSerializer;

/**
 * RedisSessionConfig class
 *
 * @author Flc
 * @date 2019/10/14
 */
//@Configuration
//@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 1800)
public class RedisSessionConfig {

    //@Bean
    public JedisConnectionFactory connectionFactory() {
        return new JedisConnectionFactory();
    }

    //@Bean
    public DefaultCookieSerializer defaultCookieSerializer() {
            DefaultCookieSerializer defaultCookieSerializer = new DefaultCookieSerializer();
            defaultCookieSerializer.setCookieName("SESSION_NAME");
            defaultCookieSerializer.setDomainName("");
            //defaultCookieSerializer().setUseHttpOnlyCookie(true);
            defaultCookieSerializer.setCookiePath("/");
            defaultCookieSerializer.setCookieMaxAge(31536000);
            return defaultCookieSerializer;
    }
}
