package com.rex.guessthenumberab.game_command.impl;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;

import java.util.Random;

public class BlessYouCommand implements PlayCommand {

    private final MessageEvent event;
    private final RedisService redisService;

    public BlessYouCommand(MessageEvent event, RedisService redisService) {
        this.event = event;
        this.redisService = redisService;
    }

    @Override
    public LineBotResponse execute() {
        String[] blessings = {
                "哥布林兔兔大祭司祝福你！你獲得了【+99邊緣感光環】，沒人會注意到你！",
                "獲得【哥布林脫單咒】，每次約會都會神秘取消，連詐騙都不理你！",
                "哥布林之神親吻你的額頭，你感受到前所未有的...寂寞。",
                "你沐浴在哥布林聖水中，獲得【社交尷尬 +100】！",
                "你抽到了【極稀有】祝福卡：哥布林祝你每天都掉手機但都撿得回來！",
                "獲得祝福：【哥布林思考迴圈】，思考為什麼沒人愛你 → 睡覺 → 重複。",
                "哥布林祭司對你念出神秘咒語，你立刻失去一切社交慾望！",
                "你被哥布林兔兔大祭司擁抱了！...現在你只想回家洗澡。"
        };

        Random random = new Random();
        String selected = blessings[random.nextInt(blessings.length)];
        return new LineBotResponse(selected);
    }
}
