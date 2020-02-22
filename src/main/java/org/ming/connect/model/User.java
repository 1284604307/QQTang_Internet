package org.ming.connect.model;

import org.ming.model.PlayerName;

public class User {
    private int id;
    private String username;
    //房间号
    private int roomIndex;
    //座位号
    private int seatIndex;
    //选择的角色
    private PlayerName playerName = PlayerName.火影;
    //状态
    private UserStatus status = UserStatus.NONE;


    public User(){

    }

    public User(String name,int id){
        this.username = name;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRoomIndex() {
        return roomIndex;
    }

    public void setRoomIndex(int roomIndex) {
        this.roomIndex = roomIndex;
    }

    public PlayerName getPlayerName() {
        return playerName;
    }

    public void setPlayerName(PlayerName playerName) {
        this.playerName = playerName;
    }

    public String getName() {
        return username;
    }

    public void setName(String name) {
        this.username = name;
    }

    public int getSeatIndex() {
        return seatIndex;
    }

    public void setSeatIndex(int seatIndex) {
        this.seatIndex = seatIndex;
    }

    public UserStatus getStatus() {
        return status;
    }

    public void setStatus(UserStatus status) {
        this.status = status;
    }
}
