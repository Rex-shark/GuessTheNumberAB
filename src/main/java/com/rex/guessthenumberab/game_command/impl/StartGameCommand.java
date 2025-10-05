package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.enums.RoomState;
import com.rex.guessthenumberab.game_command.PlayCommand;

import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.LineGameRequest;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;


import java.util.Optional;


public class StartGameCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public StartGameCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        //開始遊戲
        LineGameRequest lineGameRequest = new LineGameRequest(event);
        if (lineGameRequest.isNull()){
            return new LineBotResponse("start error");
        }

        String roomId = lineGameRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
          return new LineBotResponse("蛤!?沒有遊戲房間");
        }

        Room room = (Room) existingRoom.get();

        //判斷 room.getRoomState()是否是遊戲中
        if (room.getRoomState() == RoomState.PLAYING) {
            return new LineBotResponse("房間正在遊戲中!嗚啦嗚啦!");
        }

        room.startGame(4);
        redisService.saveRoom(roomId, room); // 更新房間資料
        String msg = room.getStartGameMessage();
        return new LineBotResponse(msg);

    }
}
