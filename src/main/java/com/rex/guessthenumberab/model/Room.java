package com.rex.guessthenumberab.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rex.guessthenumberab.enums.PlayerType;
import com.rex.guessthenumberab.enums.RoomState;
import jakarta.annotation.Resource;
import lombok.Data;
import org.springframework.stereotype.Component;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Component
@Data
public class Room implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // 序列化版本號
    private String id; // 房間 ID
    private String name; // 房間名稱
    private Player gameMaster; // 遊戲主持人
    private List<Player> gamePlayer = new ArrayList<>(); // 遊戲玩家
    private String secretNumber; // 房間的秘密數字
    private RoomState roomState; // 房間類型
    private List<GameHistory> gameHistory = new ArrayList<>(); // 遊戲歷史記錄

    @Resource
    GameHistory history ;


    public void createRoom(String id, String name, Player gameMaster) {
        this.id = id;
        this.name = name;
        this.gameMaster = gameMaster;
        this.secretNumber = ""; // 初始時沒有秘密數字
        //this.gamePlayer.add(gameMaster);
        this.roomState = RoomState.WAITING; // 初始狀態為等待
    }

//    public Room(String id, String name, Player gameMaster) {
//        this.id = id;
//        this.name = name;
//        this.gameMaster = gameMaster;
//        this.secretNumber = ""; // 初始時沒有秘密數字
//        gamePlayer.add(gameMaster);
//        this.roomState = RoomState.WAITING; // 初始狀態為等待
//    }
    //開始遊戲
    public void startGame(Integer secretNumberLength) {
        System.out.println("遊戲開始！秘密數字已生成，請玩家開始猜測。");
        this.initializeGame(secretNumberLength); // 初始化遊戲，生成秘密數字並隨機排序玩家
    }

    //初始化遊戲
    public void initializeGame(Integer secretNumberLength) {
        this.generateSecretNumber(secretNumberLength); // 生成秘密數字
        //shufflePlayers(); // 隨機排序玩家
        Player CurrentPlayer = findCurrentPlayer();
        this.roomState = RoomState.PLAYING; // 設置房間狀態為遊戲中

//        System.out.println("--------------------");
//        System.out.println("輪到玩家: " + CurrentPlayer.getName() + " 猜測秘密數字。");
//        System.out.println("秘密數字:"+ secretNumber); // 這裡可以選擇是否顯示秘密數字，方便測試
//        System.out.println("--------------------");
    }

    //顯示輪到的當前玩家
    public Player findCurrentPlayer() {
        if (gamePlayer.isEmpty()) {
            throw new IllegalStateException("房間內沒有玩家");
        }
        return gamePlayer.get(0); // 返回第一個玩家，假設第一個玩家是輪到的玩家
    }

    //輪換玩家list中的玩家順序
    public void rotatePlayers() {
        if (gamePlayer.isEmpty()) {
            throw new IllegalStateException("房間內沒有玩家");
        }
        Player firstPlayer = gamePlayer.remove(0); // 移除第一個玩家
        gamePlayer.add(firstPlayer); // 將第一個玩家添加到列表末尾
    }

    //加入玩家
    public void addPlayer(Player player) {
        //先判斷玩家是否存在
        if (gamePlayer.contains(player)) {
            throw new IllegalArgumentException("玩家已經在房間中");
        }

        if (gamePlayer.size() < 20) { // 假設最多6個玩家
            gamePlayer.add(player);
        } else {
            throw new IllegalStateException("房間已滿，無法加入更多玩家");
        }
    }


    //移除玩家
    public void removePlayer(Player player) {
        if (gamePlayer.contains(player)) {
            gamePlayer.remove(player);
        } else {
            throw new IllegalArgumentException("玩家不在房間中");
        }
    }

    //清空玩家
    public void clearPlayers() {
        gamePlayer.clear(); // 清空玩家列表
        gamePlayer.add(gameMaster); // 確保遊戲主持人仍在房間中
    }


    //依照房間內玩家，隨機排序玩家遊戲順序(包含莊家)
    public void shufflePlayers() {
        java.util.Collections.shuffle(gamePlayer); // 對所有玩家進行隨機排序
    }

    //玩家猜數字
    public String guessSecretNumber(Player player, String guess) {
        StringBuilder sb = new StringBuilder();
        System.out.println("player.getId() = " + player.getId());
        gamePlayer.forEach(p -> System.out.println("玩家: " + p.getName() + ", ID: " + p.getId()));

        if (!gamePlayer.contains(player)) {
            throw new IllegalArgumentException("玩家不在房間中");
        }
        if(!isCurrentPlayer(player)) {
            throw new IllegalStateException(player.getName()+"，不是當前玩家，請等待輪到你");
        }
        String result = checkGuess(guess);
        //System.out.println(player.getName()+" 猜測數字是: " + guess + " 猜測結果: " + result);
        sb.append(player.getName()).append(" 猜測數字是: ").append(guess).append(" 猜測結果: ").append(result).append("\n");
        addGameHistory(guess);

        if (isGuessCorrect(guess)) {
            //System.out.println(player.getName() + " 猜對了秘密數字: " + secretNumber);
            sb.append(player.getName()).append(" 猜對了秘密數字: ").append(secretNumber).append("\n");
            this.winGame(player); // 玩家猜對，遊戲結束
        } else {
            this.rotatePlayers(); // 猜錯後輪換玩家
            System.out.println("猜錯了，輪到下一位玩家 : " + findCurrentPlayer().getName());
            sb.append("猜錯了，輪到下一位玩家 : ").append(findCurrentPlayer().getName()).append("\n");
        }
        return sb.toString();
    }

    //判斷猜的玩家是否是當前玩家
    public boolean isCurrentPlayer(Player player) {
        return findCurrentPlayer().equals(player);
    }

    //勝利
    public void winGame(Player player) {
        this.roomState = RoomState.WAITING; // 設置房間狀態為結束
        this.secretNumber = ""; // 清空秘密數字

        //走訪List<Player> gamePlayer 改為輸了 ，Player player 改為獲勝
        for (Player p : gamePlayer) {
            if (p.equals(player)) {
                p.setPlayerType(PlayerType.WINNER); // 設置獲勝玩家狀態
            } else {
                p.setPlayerType(PlayerType.LOSER); // 設置其他玩家為輸家
            }
        }

    }

    //產生神秘數字
    public void generateSecretNumber(Integer length) {
        if(length == null){
            length = 4; // 默認長度為4
        }
        if (length < 1 || length > 6) {
            throw new IllegalArgumentException("秘密數字 between 1 and 6");
        }
        StringBuilder number = new StringBuilder();
        for (int i = 0; i < length; i++) {
            int digit = (int) (Math.random() * 10); // 生成0-9的隨機數字
            number.append(digit);
        }
        this.secretNumber = number.toString();
    }

    //傳入1個String，判斷是不是神秘數字，顯示幾A幾B
    public String checkGuess(String guess) {
        System.out.println("guess = " + guess);
        System.out.println("secretNumber = " + secretNumber);
        if (guess.length() != secretNumber.length()) {
            throw new IllegalArgumentException("蛤?猜測的數字長度必須與秘密數字相同");
        }

        int aCount = 0; // 正確位置的數字
        int bCount = 0; // 錯誤位置的數字

        for (int i = 0; i < guess.length(); i++) {
            if (guess.charAt(i) == secretNumber.charAt(i)) {
                aCount++;
            } else if (secretNumber.contains(String.valueOf(guess.charAt(i)))) {
                bCount++;
            }
        }

        return aCount + "A" + bCount + "B"; // 返回結果格式為 "xAyB"
    }

    //判斷是否猜對
    public boolean isGuessCorrect(String guess) {
        return guess.equals(secretNumber);
    }

    public void addGameHistory(String guess) {
        history.setPlayerId(findCurrentPlayer().getId());
        history.setPlayerName(findCurrentPlayer().getName());
        history.setSecretNumber(secretNumber);
        history.setGuess(guess);
        history.setResult(checkGuess(guess));
        this.gameHistory.add(history);
        System.out.println("遊戲歷史記錄已更新: " + history);
    }

    @JsonIgnore
    public String getStartGameMessage() {
        if (gamePlayer.isEmpty()) {
            return "房間內沒有玩家";
        }
        StringBuilder message = new StringBuilder("嗚啦!遊戲開始！秘密數字已生成，請玩家開始猜測。\n");
        //本輪玩家順序
        message.append("本輪玩家順序:\n");
        for (int i = 0; i < gamePlayer.size(); i++) {
            message.append((i + 1)).append(". ").append(gamePlayer.get(i).getName()).append("\n");
        }

       return message.toString();
    }
}
