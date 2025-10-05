package com.rex.guessthenumberab.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LineUserRequest {
    private String userId; // LINE 使用者 ID
    private String userName; // 顯示名稱
    private String groupId; // LINE 群組 ID
    private String statusMessage; // 狀態訊息
    private String guess; // 玩家猜的數字
    private Integer num;

}
