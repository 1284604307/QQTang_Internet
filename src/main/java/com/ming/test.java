package com.ming;

import com.ming.MVC.model.Item;
import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Random;

public class test  {
    private static ArrayList<Item>[][] items = new ArrayList[12][12];

    public static void main(String[] args) {

        System.out.println(Math.random());

//        if (items[1][1]==null) {
//            items[1][1] = new ArrayList<Item>();
//            items[1][1].add(new Wall(1,1));
//        }else {
//            items[1][1].add(new Wall(1,1));
//        }
//        System.out.println(items[1][1].get(0));

    }

    public void video(){
        InputStream is = null;// 获得音乐文件的输入流
        try {
            is = new File("src/Music/bubbleBomb.wav").toURI().toURL().openStream();
            AudioStream as = new AudioStream(is);//创建音频 流
            AudioPlayer.player.start(as);//开始播放

            Thread.sleep(501);
            is = new File("src/Music/bubbleBomb.wav").toURI().toURL().openStream();
            as = new AudioStream(is);//创建音频 流
            AudioPlayer.player.start(as);//开始播放


            Thread.sleep(501);
            is = new File("src/Music/bubbleBomb.wav").toURI().toURL().openStream();
            as = new AudioStream(is);//创建音频 流
            AudioPlayer.player.start(as);//开始播放
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
