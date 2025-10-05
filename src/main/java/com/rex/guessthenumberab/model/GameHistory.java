package com.rex.guessthenumberab.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@Data
@NoArgsConstructor
public class GameHistory {

    private String playerId;
    private String playerName;
    private String secretNumber;
    private String guess;
    private String result;

    @Override
    public String toString() {
        return "GameHistory{" +
                "playerId='" + playerId + '\'' +
                ", playerName='" + playerName + '\'' +
                ", secretNumber='" + secretNumber + '\'' +
                ", guess='" + guess + '\'' +
                ", result='" + result + '\'' +
                '}';
    }

}
