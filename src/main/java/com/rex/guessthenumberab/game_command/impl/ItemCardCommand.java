package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;


public class ItemCardCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public ItemCardCommand(MessageEvent event,  RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        return new LineBotResponse("蛤!?你沒有道具卡！");
    }
}
