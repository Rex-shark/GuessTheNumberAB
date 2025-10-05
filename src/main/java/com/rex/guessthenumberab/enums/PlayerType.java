package com.rex.guessthenumberab.enums;

import lombok.Getter;

@Getter
public enum PlayerType {

    WAITING("等待", 1),
    FINISHED("已完成", 2),
    WINNER("獲勝", 3),
    LOSER("輸了", 4);

    private final String description;
    private final int code;

    PlayerType(String description, int code) {
        this.description = description;
        this.code = code;
    }
}
