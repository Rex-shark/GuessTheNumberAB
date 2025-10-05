package com.rex.guessthenumberab.model;


import com.rex.guessthenumberab.enums.PlayerType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.Objects;


@Data
public abstract class Player implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L; // 序列化版本號
    protected String id; // 玩家 ID
    protected String name; // 玩家名稱
    protected PlayerType playerType; // 玩家狀態

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Player player = (Player) obj;
        return id != null && id.equals(player.id); // 根據玩家 ID 判斷是否相等
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
