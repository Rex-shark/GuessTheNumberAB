package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;

import java.util.LinkedHashMap;
import java.util.Map;

public class HelpCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public HelpCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        Map<String, String> commandDescriptions = new LinkedHashMap<>();
        commandDescriptions.put("-建立 您的暱稱", "建立一個房間");
        commandDescriptions.put("-加入 您的暱稱", "加入一個房間");
        commandDescriptions.put("-開始", "開始遊戲");
        commandDescriptions.put("-猜 你的數字", "猜數字");
        commandDescriptions.put("-刪除", "刪除房間");
        commandDescriptions.put("-祈禱", "獲得大祭司的祝福");
        commandDescriptions.put("-道具", "使用道具卡");
        //commandDescriptions.put("-show", "顯示所有指令");

        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : commandDescriptions.entrySet()) {
            sb.append(entry.getKey()).append(":").append(entry.getValue()).append("\n");
        }
        return new LineBotResponse(sb.toString());

    }
}
