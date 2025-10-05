package com.rex.guessthenumberab.controller;

import com.rex.guessthenumberab.domain.LineUserRequest;
import com.rex.guessthenumberab.enums.RoomState;
import com.rex.guessthenumberab.model.*;
import com.rex.guessthenumberab.redis.RedisService;
import com.rex.guessthenumberab.response.ApiResponse;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/api/line/guess/game")
public class GameController {


    @Resource
    Room room;

    @Resource
    private RedisService redisService;

    long timeout = 0; // 永久有效

    TimeUnit unit = TimeUnit.HOURS; // 時間單位設置為小時

    @PostMapping("/create")
    public ResponseEntity<ApiResponse> createRoom(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        String roomName = lineUserRequest.getGroupId() + " Room";

        GameMaster gameMaster = new GameMaster(lineUserRequest.getUserId(), lineUserRequest.getUserName());
        GamePlayer gamePlayer = new GamePlayer(lineUserRequest.getUserId(), lineUserRequest.getUserName());
        //先去 redis找room房間是否存在
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isPresent()) {
            ApiResponse response = new ApiResponse("Room already exists", null);
            return ResponseEntity.status(409).body(response); // HTTP 409 表示衝突

        } else {
            // 房間不存在，執行創建邏輯
            room.createRoom(roomId, roomName, gamePlayer);
            room.addPlayer(new GamePlayer(lineUserRequest.getUserId(), lineUserRequest.getUserName()));
            // 將房間存入 Redis，設置為永久有效
            redisService.saveRoom(roomId, room, timeout, null);
            ApiResponse response = new ApiResponse("Room created successfully", room);
            return ResponseEntity.status(200).body(response);
        }

    }

    //加入玩家
    @PostMapping("/join")
    public ResponseEntity<ApiResponse> joinRoom(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
            ApiResponse response = new ApiResponse("Room not found", null);
            return ResponseEntity.status(404).body(response); // HTTP 404 表示未找到
        }

        try {
            Room room = (Room) existingRoom.get();
            room.addPlayer(new GamePlayer(lineUserRequest.getUserId(), lineUserRequest.getUserName()));
            redisService.saveRoom(roomId, room, timeout, unit); // 更新房間資料
            String msg = lineUserRequest.getUserName() + "，加入遊戲！";
            ApiResponse response = new ApiResponse(msg, room);
            return ResponseEntity.status(200).body(response);
        }catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse("Error joining room: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response); // HTTP 500 表示內部服務器錯誤
        }

    }

    //開始遊戲
    @PostMapping("/start")
    public ResponseEntity<ApiResponse> startGame(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
            ApiResponse response = new ApiResponse("Room not found", null);
            return ResponseEntity.status(404).body(response); // HTTP 404 表示未找到
        }

        Room room = (Room) existingRoom.get();

        //判斷 room.getRoomState()是否是遊戲中
        if (room.getRoomState() == RoomState.PLAYING) {
            ApiResponse response = new ApiResponse("房間正在遊戲中", null);
            return ResponseEntity.status(400).body(response); // HTTP 400 表示錯誤請求
        }

        room.startGame(lineUserRequest.getNum()); // 假設 Room 類有 startGame 方法
        redisService.saveRoom(roomId, room, timeout, unit); // 更新房間資料
        String msg = room.getStartGameMessage();
        ApiResponse response = new ApiResponse(msg, room);
        return ResponseEntity.status(200).body(response);
    }

    //玩家猜數字
    @PostMapping("/guess")
    public ResponseEntity<ApiResponse> guessNumber(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
            ApiResponse response = new ApiResponse("Room not found", null);
            return ResponseEntity.status(404).body(response); // HTTP 404 表示未找到
        }

        //判斷 room.getRoomState()是否是遊戲中
        if (room.getRoomState() != RoomState.PLAYING) {
            ApiResponse response = new ApiResponse("房間還沒開始", null);
            return ResponseEntity.status(400).body(response); // HTTP 400 表示錯誤請求
        }

        String guessResult ;
        try {
            Room room = (Room) existingRoom.get();
            Player currentPlayer = room.findCurrentPlayer();

            if (!currentPlayer.getId().equals(lineUserRequest.getUserId())) {
                ApiResponse response = new ApiResponse("不是你的回合", null);
                return ResponseEntity.status(403).body(response); // HTTP 403 表示禁止訪問
            }
            Player GamePlayer = new GamePlayer(lineUserRequest.getUserId(), lineUserRequest.getUserName());
            guessResult = room.guessSecretNumber(GamePlayer, String.valueOf(lineUserRequest.getGuess()));
            redisService.saveRoom(roomId, room, timeout, unit); // 更新房間資料

        } catch (IllegalArgumentException e) {
            ApiResponse response = new ApiResponse("Error room: " + e.getMessage(), null);
            return ResponseEntity.status(500).body(response); // HTTP 500 表示內部服務器錯誤
        }

        ApiResponse response = new ApiResponse(guessResult, room);
        return ResponseEntity.status(200).body(response);
    }

    //user 查房間資訊
    @PostMapping("/info")
    public ResponseEntity<ApiResponse> getRoomInfo(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        Optional<Object> existingRoom = redisService.getRoom(roomId);

        if (existingRoom.isEmpty()) {
            ApiResponse response = new ApiResponse("Room not found", null);
            return ResponseEntity.status(404).body(response); // HTTP 404 表示未找到
        }

        Room room = (Room) existingRoom.get();
        String roomInfo = getRoomInfo(room);
        ApiResponse response = new ApiResponse(roomInfo, null);
        return ResponseEntity.status(200).body(response);
    }


    //刪除
    @PostMapping("/delete")
    public ResponseEntity<ApiResponse> deleteRoom(@RequestBody LineUserRequest lineUserRequest) {
        String roomId = lineUserRequest.getGroupId();
        redisService.deleteRoomByKey(roomId);
        ApiResponse response = new ApiResponse("房間已刪除", null);
        return ResponseEntity.status(200).body(response);
    }

    private String getRoomInfo(Room room) {
        StringBuilder info = new StringBuilder();
        //info.append("Room ID: ").append(room.getId()).append("\n");
        info.append("Room Name: ").append(room.getName()).append("\n");
        info.append("Room State: ").append(room.getRoomState()).append("\n");
        info.append("Players:\n");
        for (Player player : room.getGamePlayer()) {
            //info.append(" - ").append(player.getName()).append(" (ID: ").append(player.getId()).append(")\n");
            info.append(" - ").append(player.getName()).append(" (Type: ").append(player.getPlayerType()).append(")\n");
        }
        info.append("當前 Player: ").append(room.findCurrentPlayer().getName()).append("\n");
//        info.append("Secret Number: ").append(room.getSecretNumber()).append("\n");
//        info.append("Game History:\n");
//        List<GameHistory> gameHistory = room.getGameHistory();
//        for (GameHistory history : gameHistory) {
//            info.append(" - ").append(history.getGuess()).append(" : ").append(history.getResult()).append("\n");
//        }

        return info.toString();


    }
}
