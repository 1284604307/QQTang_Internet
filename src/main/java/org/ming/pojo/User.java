package org.ming.pojo;

import org.ming.model.PlayerName;

public class User {
    private int id;
    private String name;
    //房间号
    private int roomIndex;
    //座位号
    private int seatIndex;
    //选择的角色
    private PlayerName playerName = PlayerName.火影;

    public User(){

    }

    public User(String name,int id){
        this.name = name;
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
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSeatIndex() {
        return seatIndex;
    }

    public void setSeatIndex(int seatIndex) {
        this.seatIndex = seatIndex;
    }
}
