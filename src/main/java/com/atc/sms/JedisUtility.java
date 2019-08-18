package com.atc.sms;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

@Service
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
