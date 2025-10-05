package com.rex.guessthenumberab.redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class RedisService {
    private static final String PREFIX = "guess_room:"; // 定義前墜
    private final RedisTemplate<String, Object> redisTemplate;

    private long timeout = 24; // 0 = 永久有效

    private TimeUnit unit = TimeUnit.HOURS; // 時間單位設置為小時

    public RedisService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void saveRoom(String key, Object room, long timeout, TimeUnit unit) {
        String prefixedKey = PREFIX + key; // 增加前墜
        if (timeout > 0 && unit != null) {
            redisTemplate.opsForValue().set(prefixedKey, room, timeout, unit);
        } else {
            redisTemplate.opsForValue().set(prefixedKey, room);
        }
    }

    public void saveRoom(String key, Object room) {
       this.saveRoom(key, room, timeout, unit); // 使用默認
    }

    public Optional<Object> getRoom(String key) {
        String prefixedKey = PREFIX + key; // 增加前墜
        return Optional.ofNullable(redisTemplate.opsForValue().get(prefixedKey));
    }

    //查所有房間的鍵
    public Iterable<String> getAllRoomKeys() {
        return redisTemplate.keys(PREFIX + "*"); // 查詢所有帶前墜的鍵
    }

    public void deleteRoomByKey(String key) {
        String prefixedKey = PREFIX + key;
        //System.out.println("prefixedKey = " + prefixedKey);
        redisTemplate.delete(prefixedKey);
    }
}
