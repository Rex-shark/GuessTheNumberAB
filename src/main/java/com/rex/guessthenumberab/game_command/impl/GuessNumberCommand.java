package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.*;
import com.rex.guessthenumberab.redis.RedisService;

import java.util.Optional;


public class GuessNumberCommand implements PlayCommand {

    private final MessageEvent event;

    private final RedisService redisService;

    public GuessNumberCommand(MessageEvent event,RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        //開始遊戲
        LineGameRequest lineGameRequest = new LineGameRequest(event);
        if (lineGameRequest.isNull()){
            return new LineBotResponse("Request is null or invalid");
        }

        String roomId = lineGameRequest.getGroupId();
        String userId = lineGameRequest.getUserId();
        String guess =  lineGameRequest.getTextParameter();

        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
          return new LineBotResponse("Room not found");
        }


        String guessResult ;
        try {
            Room room = (Room) existingRoom.get();
            Player currentPlayer = room.findCurrentPlayer();
            String userName = currentPlayer.getName();

            if (!currentPlayer.getId().equals(lineGameRequest.getUserId())) {
               return new LineBotResponse("不是你的回合!嗚啦!");
            }
            Player GamePlayer = new GamePlayer(userId,userName);
            guessResult = room.guessSecretNumber(GamePlayer, String.valueOf(guess));
            redisService.saveRoom(roomId, room); // 更新房間資料

        } catch (IllegalArgumentException e) {
            return new LineBotResponse(e.getMessage());
        }

        return new LineBotResponse(guessResult);

    }
}
