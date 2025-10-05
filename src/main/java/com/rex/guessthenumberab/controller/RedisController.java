package com.rex.guessthenumberab.controller;


import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/sys/redis")
public class RedisController {

    @Resource
    private RedisService redisService;


    @GetMapping("/room/{roomId}")
    public Room getRoomFromRedis(@PathVariable String roomId) {
        // 從 Redis 中獲取房間資料
        Optional<Object> room = redisService.getRoom(roomId);
        // 將 Optional 轉換為 Room 對象
        // 如果房間不存在，返回 null
        return (Room) room.orElse(null);

    }

    @GetMapping("/rooms")
    public Iterable<String> getAllRoomsFromRedis() {
        // 從 Redis 中獲取所有房間資料
        return  redisService.getAllRoomKeys() ;
    }
    @DeleteMapping("/room/{roomId}")
    public String deleteRoomFromRedis(@PathVariable String roomId) {
        // 刪除指定房間資料
        redisService.deleteRoomByKey(roomId);
        return "Room deleted successfully";
    }
}
