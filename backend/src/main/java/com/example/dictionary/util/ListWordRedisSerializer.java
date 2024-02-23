package com.example.dictionary.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.dictionary.entities.Word;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;



public class ListWordRedisSerializer implements RedisSerializer<Word[]> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public byte[] serialize(Word[] words) throws SerializationException {
        try {
            return objectMapper.writeValueAsBytes(words);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error serializing List<Word> to JSON", e);
        }
    }

    @Override
    public Word[] deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null) {
            return null;
        }
        try {
            String jsonString = new String(bytes);

            objectMapper.readValue(jsonString, Word[].class);
            return objectMapper.readValue(jsonString, Word[].class);
        } catch (JsonProcessingException e) {
            throw new SerializationException("Error deserializing JSON to List<Word>", e);
        }
    }
}
