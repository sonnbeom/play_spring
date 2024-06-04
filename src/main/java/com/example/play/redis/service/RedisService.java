package com.example.play.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> redisTemplate;

    public void setValues(String key, String value){
        redisTemplate.opsForValue().set(key, value);
        //해당 키가 이미 존재한다면 기존의 값을 새로운 값으로 덮어씁니다.
    }
    // 만료시간 설정 -> 자동 삭제
    public void setValuesWithTimeOut(String key, String value, long timeOut){
        redisTemplate.opsForValue().set(key, value, timeOut, TimeUnit.MILLISECONDS);
    }
    public String getValues(String key){
        return redisTemplate.opsForValue().get(key);
    }
    public void deleteValues(String key){
        redisTemplate.delete(key);
    }
}
