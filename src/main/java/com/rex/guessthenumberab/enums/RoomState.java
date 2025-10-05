package com.rex.guessthenumberab.enums;

import lombok.Getter;

@Getter
public enum RoomState {
    WAITING("等待", 1),
    PLAYING("進行中", 2);

    private final String description;
    private final int code;

    RoomState(String description, int code) {
        this.description = description;
        this.code = code;
    }

}
