package com.rex.guessthenumberab.handler;


import com.linecorp.bot.client.base.Result;
import com.linecorp.bot.messaging.model.Message;
import com.linecorp.bot.messaging.model.UserProfileResponse;
import com.linecorp.bot.webhook.model.*;
import com.rex.guessthenumberab.model.LineBotResponse;
import com.rex.guessthenumberab.service.LineGameService;
import jakarta.annotation.Resource;

import com.linecorp.bot.messaging.model.TextMessage;
import com.linecorp.bot.spring.boot.handler.annotation.EventMapping;
import com.linecorp.bot.spring.boot.handler.annotation.LineMessageHandler;
import lombok.extern.slf4j.Slf4j;



@Slf4j
@LineMessageHandler
public class LinebotHandler {

    @Resource
    LineGameService lineGameService;


//
//
    @EventMapping
    public Message handleTextMessageEvent(MessageEvent event) {

        LineBotResponse lineBotResponse ;
        System.out.println("\"aaa\" = " + "aaa");
        System.out.println("event = " + event);
        String userId = null;
        Source source = event.source();
        if (source instanceof GroupSource groupSource) {
            System.out.println("🐾 這是群組訊息喵！");
            System.out.println("群組 ID：" + groupSource.groupId());
            MessageContent content = event.message();
            userId = source.userId();
            System.out.println("userId = " + userId);


           // System.out.println("🐾 使用者名稱：" + userName);

            if (content instanceof TextMessageContent textMessage) {
                System.out.println("🗨️ 使用者傳的文字：" + textMessage.text());
                //找出使用者id




            } else if (content instanceof ImageMessageContent imageMessage) {
                System.out.println("🖼️ 使用者傳了一張圖片喵！");
                System.out.println("圖片 ID：" + imageMessage.id());
            } else if (content instanceof StickerMessageContent sticker) {
                System.out.println("💬 使用者傳了貼圖！");
                System.out.println("貼圖 ID：" + sticker.stickerId());
            } else {
                System.out.println("📦 其他訊息類型：" + content.getClass().getSimpleName());
            }

        } else if (source instanceof RoomSource roomSource) {
            System.out.println("🐾 這是多人聊天室訊息喵！");
            System.out.println("聊天室 ID：" + roomSource.roomId());
        } else if (source instanceof UserSource userSource) {
            System.out.println("🐾 這是個人訊息喵！");
            System.out.println("使用者 ID：" + userSource.userId());
        } else {
            System.out.println("😿 無法辨識來源類型喵！");
        }


        try{
            lineBotResponse = lineGameService.PlayLineGame(event);
        } catch (IllegalStateException e) {
            log.error("處理事件時發生錯誤: {}", e.getMessage(), e);
            return new TextMessage(e.getMessage());
        }
        //System.out.println("lineBotResponse = " + lineBotResponse);


        final String originalMessageText = lineBotResponse.isSuccess() ? lineBotResponse.getMessage() : "指令錯誤，請重新輸入";

        if (originalMessageText == null || originalMessageText.isEmpty()) {
            return this.getRandomResponse(); // 如果沒有回應內容，則不發送任何消息
        }

        return new TextMessage(originalMessageText);
        //新模組 單人
        //event = MessageEvent[source=UserSource[userId=U4572b96a8d20e5523f085c51e2205507]
        // , timestamp=1751795411970, mode=ACTIVE, webhookEventId=01JZFJ9BFXFP385FQBFWQV26FY
        // , deliveryContext=DeliveryContext[isRedelivery=false]
        // , replyToken=444941425b034eb0b3ce149deeb2c1a1
        // , message=TextMessageContent[id=568687405549486235, text=0, emojis=null, mention=null, quoteToken=zVKIS4cN28qUPBYvl-YlQKxdyWoqJfhQiJXt2yQC0R3KYihdRfQX97xgF-qC_MPFI4wMc8R-nrn5KNnLV_bvrhFfbLZFNgR-muCxD4yIbUO2Zhg13zP2Ekg2VvuqPBID5kw6To9WhPTtr1pUej9vEw, quotedMessageId=null]]
        //新模組 群組
        //a event = MessageEvent[source=GroupSource[groupId=C1262e413e56d7405a9f86aa0da8280fe, userId=U4572b96a8d20e5523f085c51e2205507], timestamp=1751808478086, mode=ACTIVE, webhookEventId=01JZFYR31RPMJ14TGNQG799YH8, deliveryContext=DeliveryContext[isRedelivery=false], replyToken=32b676b1f51145078089f0c733530da9, message=TextMessageContent[id=568709326844199520, text=., emojis=null, mention=null, quoteToken=moNxkWo4VOResAgc-N5xvH2VSJgL-97YzLV5zNnPWq29cSOdFGiPNaOHLj4EgZCXgyQ4tzPy2W5gkdzcxU64qLN6j7jY-nGP_bKGmN5Kv1mvS3NM7UQCe5O-QsLx1balPVDH293JSxMXMJ8DoMyJkg, quotedMessageId=null]]


        //單人
        //內容記錄TextMessageContent(id=568680190742626666, text=0, emojis=null, mention=null)
        //內容記錄MessageEvent(replyToken=9f2066c6d919432a843e715f5887dafc
        // , source=UserSource(userId=U4572b96a8d20e5523f085c51e2205507)
        // , message=TextMessageContent(id=568680190742626666, text=0, emojis=null, mention=null)
        // , timestamp=2025-07-06T08:38:31.614Z, mode=ACTIVE, webhookEventId=01JZFE63ZJYG87AFBC3TE6M981
        // , deliveryContext=DeliveryContext(isRedelivery=false))

        //群組
        //內容記錄MessageEvent(replyToken=0cd6d7cadce343c08298c8df81723755
        //內容記錄messageContent = TextMessageContent(id=568680530112675842, text=0, emojis=null, mention=null)
        // , source=GroupSource(groupId=C1262e413e56d7405a9f86aa0da8280fe
        // , userId=U4572b96a8d20e5523f085c51e2205507)
        // , message=TextMessageContent(id=568680530112675842, text=0, emojis=null, mention=null)
        // , timestamp=2025-07-06T08:41:53.929Z, mode=ACTIVE, webhookEventId=01JZFEC9G2Q0MKTQ3A1R3X8SAC
        // , deliveryContext=DeliveryContext(isRedelivery=false))


    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        //System.out.println("b event: " + event);
    }

    //臨時性設計 隨機回應文字
    private Message getRandomResponse() {
        //設計一個隨機機率1/100，才會隨機回答
        //如果隨機數小於1/100，則回應隨機文字
        if (Math.random() > 0.01) {
            return null; // 99% 機率不回應隨機文字
        }

        String[] responses = {
            "嗚啦呀哈！",
            "呀哈嗚拉！",
            "嗚拉呀哈嗚拉！",
            "嗚啦！",
            "呀哈！",
            "蛤！",
            "哼！",
            "噗魯魯魯魯！",
            "嗚啦呀哈！呀哈嗚拉",
            "噗哩！",

        };
        int randomIndex = (int) (Math.random() * responses.length);
        return new TextMessage(responses[randomIndex]);
    }
}
