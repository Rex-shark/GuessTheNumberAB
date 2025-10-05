package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.GamePlayer;
import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.LineGameRequest;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;

import java.util.Optional;


public class JoinGameCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public JoinGameCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        //加入遊戲
        LineGameRequest lineGameRequest = new LineGameRequest(event);
        if (lineGameRequest.isNull()){
            return new LineBotResponse("join error");
        }

        String roomId = lineGameRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
          return new LineBotResponse("房間錯誤");
        }
        String userName = lineGameRequest.getTextParameter();
        try {
            Room room = (Room) existingRoom.get();
            room.addPlayer(new GamePlayer(lineGameRequest.getUserId(),userName));
            redisService.saveRoom(roomId, room); // 更新房間資料
            return new LineBotResponse(userName + "，加入遊戲！");
        }catch (IllegalArgumentException e) {
            return new LineBotResponse("Error joining room: " + e.getMessage());
        }

    }
}
