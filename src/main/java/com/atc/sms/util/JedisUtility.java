package com.atc.sms.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

@Component
public class JedisUtility {

    private Jedis jedis;

    @Value("${spring.redis.host}")
    private String host;

    public Jedis getJedis() {
        if (jedis == null) {
            jedis = new Jedis(host);
        }
        return jedis;
    }

    public void setJedis(Jedis jedis) {
        this.jedis = jedis;
    }
}
