package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.GroupSource;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.*;
import com.rex.guessthenumberab.redis.RedisService;
import com.rex.guessthenumberab.response.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Scope;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.concurrent.TimeUnit;


public class CreateGameCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public CreateGameCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        LineGameRequest lineGameRequest = new LineGameRequest(event);

        String groupId = lineGameRequest.getGroupId();
        String userId = lineGameRequest.getUserId();
        String userName = lineGameRequest.getTextParameter();
        System.out.println("userName = " + userName);

        String roomName = lineGameRequest.getTextParameter();


        GameMaster gameMaster = new GameMaster(userId, userName);
        GamePlayer gamePlayer = new GamePlayer(userId, userName);
        //先去 redis找room房間是否存在
        Optional<Object> existingRoom = redisService.getRoom(groupId);

        if (existingRoom.isPresent()) {
            return new LineBotResponse("房間已存在，請勿重複創建");
        } else {
            // 房間不存在，執行創建邏輯
            Room room = new Room();
            room.createRoom(groupId, roomName, gamePlayer);
            System.out.println("bb room = " + room);
            room.addPlayer(new GamePlayer(userId, userName));
            // 將房間存入 Redis，設置為永久有效
            redisService.saveRoom(groupId, room);
            ApiResponse response = new ApiResponse("Room created successfully", room);
            return new LineBotResponse( userName+"房間創建成功。嗚拉呀哈 呀哈嗚拉！！！");
        }

    }
}
