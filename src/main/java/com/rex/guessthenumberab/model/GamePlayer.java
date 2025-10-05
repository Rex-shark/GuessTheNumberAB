package com.rex.guessthenumberab.model;

import com.rex.guessthenumberab.enums.PlayerType;
import lombok.NoArgsConstructor;

import java.io.Serial;

@NoArgsConstructor
public class GamePlayer extends Player {

    @Serial
    private static final long serialVersionUID = 1L; // 序列化版本號

    public GamePlayer(String id, String name) {
        this.id = id;
        this.name = name;
        this.playerType = PlayerType.WAITING; // 初始狀態為等待
    }

    public GamePlayer( GameMaster gameMaster) {
        this.id = gameMaster.id;
        this.name = gameMaster.name;
        this.playerType = PlayerType.WAITING; // 初始狀態為等待
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public String toString() {
        return "GamePlayer{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", playerType=" + playerType +
                '}';
    }
}
