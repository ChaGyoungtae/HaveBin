package com.HaveBinProject.HaveBin.MailAuth;

import com.HaveBinProject.HaveBin.User.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class EmailAuthService {

    private final RedisTemplate<String, String> redisTemplate;

//    public void saveData(String key, String value) {
//        redisTemplate.opsForValue().set(key, value);
//    }

    public String getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public void saveDataWithExpiration(String key, String value, long timeoutInSeconds) {
        redisTemplate.opsForValue().set(key, value);
        redisTemplate.expire(key, timeoutInSeconds, TimeUnit.SECONDS);
    }

}
