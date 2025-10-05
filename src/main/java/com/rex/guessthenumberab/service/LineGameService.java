package com.rex.guessthenumberab.service;


import com.linecorp.bot.webhook.model.MessageEvent;
import com.rex.guessthenumberab.game_command.factory.CommandFactory;
import com.rex.guessthenumberab.game_command.PlayCommand;
import com.rex.guessthenumberab.model.LineBotResponse;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LineGameService {


    @Resource
    private CommandFactory commandFactory; // 注入 CommandFactory 實例
    // 讀取Line訊息模型，作為判斷之後行為的第一個關卡
    public LineBotResponse PlayLineGame(MessageEvent event) {
        PlayCommand  playCommand = commandFactory.getCommand(event);
        if (playCommand != null) {
            return playCommand.execute();
        } else {
            return new LineBotResponse("");
        }

    }
}
