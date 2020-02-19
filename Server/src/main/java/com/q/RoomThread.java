package com.q;

import org.ming.connect.model.User;

import java.util.ArrayList;

public class RoomThread extends Thread {

    private int roomIndex;
    private ArrayList<User> users = new ArrayList<>();
    private User master;
    private int startTimer = 0;
    private boolean start = true;

    public RoomThread(int RoomIndex,User master) {
        super();
        roomIndex = RoomIndex;
        this.master = master;

    }

    @Override
    public void run() {

    }

    public void startGameLoop(){
//        Timer timer = new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                start = false;
//                timer.cancel();
//            }
//        },3000);
        while (start){

            try {
                startTimer+=16;

                System.out.println(startTimer/60000);
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            start=false;
        }
    }
}
