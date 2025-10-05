package com.rex.guessthenumberab.model;

import com.linecorp.bot.webhook.model.GroupSource;
import com.linecorp.bot.webhook.model.MessageEvent;
import com.linecorp.bot.webhook.model.TextMessageContent;
import com.linecorp.bot.webhook.model.UserSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineGameRequest {
    private String userId; // LINE 使用者 ID
    private String groupId; // LINE 群組 ID
    private String text;

    public LineGameRequest(MessageEvent event) {
        if (event.message() instanceof TextMessageContent textMessageContent) {
            text = textMessageContent.text();
        }
        if (event.source() instanceof GroupSource groupSource) {
            // 如果是群組消息，則獲取群組 ID
            groupId = groupSource.groupId();
            userId = event.source().userId();
        }
        if (event.source() instanceof UserSource userSource) {
            // 如果是個人消息，則獲取使用者 ID
            groupId = event.source().userId();
            userId = event.source().userId();
        }

    }

    public boolean isNull() {
        return userId == null || userId.isEmpty() ||
                groupId == null || groupId.isEmpty() ||
                text == null || text.isEmpty();
    }

    public String getTextParameter() {
        String[] parts = text.split(" ", 2); // 使用空格分隔，最多分成兩部分
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            return userId; // 如果沒有提供名稱，則使用 userId
        }
        String text = parts[1].trim();
        if (text.length() > 20) { // 判斷名稱長度是否超過20
            return userId; // 如果超過20，則使用 userId
        }
        return text;
    }
}
