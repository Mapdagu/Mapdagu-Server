package com.project.mapdagu.util;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class RedisUtil {
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisTemplate<String, Object> redisBlackListTemplate;

    public void set(String key, String o, Integer minutes) {
        redisTemplate.opsForValue().set(key, o, minutes, TimeUnit.MINUTES);
    }

    public Object get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public boolean delete(String key) {
        return redisTemplate.delete(key);
    }

    public boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void setBlackList(String key, String o, Integer milliSeconds) {
        redisBlackListTemplate.opsForValue().set(key, o, milliSeconds, TimeUnit.MILLISECONDS);
    }

    public Object getBlackList(String key) {
        return redisBlackListTemplate.opsForValue().get(key);
    }

    public boolean deleteBlackList(String key) {
        return redisBlackListTemplate.delete(key);
    }

    public boolean hasKeyBlackList(String key) {
        return redisBlackListTemplate.hasKey(key);
    }
}