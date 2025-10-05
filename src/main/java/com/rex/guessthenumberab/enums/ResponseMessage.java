package com.rex.guessthenumberab.enums;

import lombok.Getter;

@Getter
public enum ResponseMessage {
    //開始遊戲
    GAME_STARTED(0);


    private final int code;
    ResponseMessage( int code) {
        this.code = code;
    }
}
