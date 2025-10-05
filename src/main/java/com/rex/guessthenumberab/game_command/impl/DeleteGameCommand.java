package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;

import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.LineGameRequest;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;


import java.util.Optional;


public class DeleteGameCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public DeleteGameCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        //刪除遊戲
        LineGameRequest lineGameRequest = new LineGameRequest(event);
        if (lineGameRequest.isNull()){
            return new LineBotResponse("Delete error");
        }

        String roomId = lineGameRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
          return new LineBotResponse("蛤?沒有遊戲房間");
        }

        try {
            Room room = (Room) existingRoom.get();

            //判斷是不是主持人
            if (!room.getGameMaster().getId().equals(lineGameRequest.getUserId())) {
                return new LineBotResponse("只有主持人:"+
                        room.getGameMaster().getName()+"可以刪除房間!烏啦!");
            }
            redisService.deleteRoomByKey(roomId);
            return new LineBotResponse("房間已刪除，烏啦呀哈！");
        }catch (IllegalArgumentException e) {
            return new LineBotResponse("Error joining room: " + e.getMessage());
        }

    }
}
