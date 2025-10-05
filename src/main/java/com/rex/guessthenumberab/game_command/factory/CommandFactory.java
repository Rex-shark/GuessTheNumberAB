package com.rex.guessthenumberab.game_command.factory;


import com.linecorp.bot.webhook.model.GroupSource;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.rex.guessthenumberab.game_command.impl.*;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.Room;
import com.rex.guessthenumberab.redis.RedisService;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Component
@Scope("prototype")
public class CommandFactory {

    @Resource
    private RedisService redisService;

    private Map<String, Function<MessageEvent, PlayCommand>> commandMap = new LinkedHashMap<>();

    @PostConstruct
    private void init() {

        commandMap.put("-建立", e -> new CreateGameCommand(e,redisService));
        commandMap.put("-加入", e -> new JoinGameCommand(e, redisService));
        commandMap.put("-開始", e -> new StartGameCommand(e,  redisService));
        commandMap.put("-猜", e -> new GuessNumberCommand(e,  redisService));
        commandMap.put("-刪除", e -> new DeleteGameCommand(e,  redisService));
        commandMap.put("-祈禱", e -> new BlessYouCommand(e, redisService));
        commandMap.put("-道具", e -> new ItemCardCommand(e,  redisService));
        commandMap.put("-help", e -> new HelpCommand(e,  redisService));
    }

    public  PlayCommand getCommand(MessageEvent event) {
        if (!(event.message() instanceof TextMessageContent textMessageContent)) {
            log.error("event.message() is not TextMessageContent: {}", event.message());
            return null;
        }
        String text = textMessageContent.text();
//        if (!(event.source() instanceof GroupSource)) {
//            //群組發送
//            log.error("event.source() is not GroupSource: {}", event.source());
//            return null;
//        }
        for (var entry : commandMap.entrySet()) {
            if (text.startsWith(entry.getKey())) {
                return entry.getValue().apply(event);
            }
        }
        return null;
    }
}
