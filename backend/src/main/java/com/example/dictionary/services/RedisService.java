package com.example.dictionary.services;

import com.example.dictionary.entities.Word;
import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {


    private final RedisTemplate<String, Word> redisTemplate;

    @Resource(name = "redisTemplate")
    private ListOperations<String, Word[]> listOps;

    public void add(String key, Word[] value) {
        listOps.leftPush(key, value);
    }

    public Word[] get(String key) {
        return listOps.leftPop(key);
    }
}
