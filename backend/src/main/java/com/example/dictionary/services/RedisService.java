package com.example.dictionary.services;

import com.example.dictionary.entities.Word;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, List<Word>> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, List<Word>> listOps;

    public void add(String key, List<Word> value) {
        listOps.leftPushAll(key, value);
    }

    public List<Word> get(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }
}
