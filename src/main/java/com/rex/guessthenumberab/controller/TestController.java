package com.rex.guessthenumberab.controller;

import com.rex.guessthenumberab.model.GameHistory;
import com.rex.guessthenumberab.model.GameMaster;
import com.rex.guessthenumberab.model.GamePlayer;
import com.rex.guessthenumberab.model.Room;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {

    @Resource
    Room room;

    //test
    @GetMapping()
    @ResponseBody
    public ResponseEntity<String> test() {
        System.out.println("test");
        return ResponseEntity.ok("Test endpoint reached successfully");
    }



    @GetMapping("demo")
    @ResponseBody
    public ResponseEntity<String> demo()  {
        System.out.println("demo");
        GameMaster gameMaster = new GameMaster("0", "Rex");
        GamePlayer gamePlayer1 = new GamePlayer("1", "Player1");
        GamePlayer gamePlayer2 = new GamePlayer("2", "Player2");
        GamePlayer gamePlayer3 = new GamePlayer("3", "Player3");

        //Room room = new Room("room1", "Test Room",gameMaster );
        room.createRoom("room1", "Test Room",gameMaster );
        room.addPlayer(new GamePlayer("room1","Test Room"));

        room.addPlayer(gamePlayer1);
        room.addPlayer(gamePlayer2);
        //room.removePlayer(gamePlayer3);

        room.startGame(4);

        room.guessSecretNumber(gameMaster,"1434");
        room.guessSecretNumber(gamePlayer1,"1254");
        room.guessSecretNumber(gamePlayer2,"1245");

        List<GameHistory> gameHistorys = room.getGameHistory();
        System.out.println("gameHistorys = " + gameHistorys);


        return ResponseEntity.ok("Demo endpoint reached successfully");
    }
}
