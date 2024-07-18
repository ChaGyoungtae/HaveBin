package com.HaveBinProject.HaveBin.Trashcan;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ReportIntervalService {

    private final RedisTemplate<String, String> redisTemplate;

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void reportWithExpiration(String key, String value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
    }
}
