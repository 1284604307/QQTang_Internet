package com.ming;

import sun.audio.AudioPlayer;
import sun.audio.AudioStream;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

public class MusicManager {
//	final static File BGM=new File("src/Music/bgm.wav");
//	final static File putBubble=new File("src/Music/putBubble.wav");
//	final static File bubbleBomb=new File("src/Music/bubbleBomb.wav");
//	final static File getGoods=new File("src/Music/getGoods.wav");

//	public final static AudioClip bubbleBomb ;
//	public final static AudioClip putBubble ;
//	public final static AudioClip getGoods ;
//	public final static AudioClip BGM ;


	public static AudioStream bubbleBomb ;
	public static AudioStream putBubble ;
	public static AudioStream getGoods ;
	public static AudioStream BGM ;

	static {
		URL bubbleBomb_url = null;
		URL putBubble_url = null;
		URL getGoods_url = null;
		URL BGM_url = null;
		try {
			bubbleBomb_url = new File("src/main/resources/Music/bubbleBomb.wav").toURI().toURL();
			putBubble_url = new File("src/main/resources/Music/putBubble.wav").toURI().toURL();
			getGoods_url = new File("src/main/resources/Music/getGoods.wav").toURI().toURL();
			BGM_url = new File("src/main/resources/Music/bgm.wav").toURI().toURL();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}

		try {
			bubbleBomb =new AudioStream(bubbleBomb_url.openStream()) ;
			putBubble =new AudioStream(putBubble_url.openStream());
			getGoods = new AudioStream(getGoods_url.openStream());
			BGM = new AudioStream(BGM_url.openStream());
		} catch (IOException e) {
			e.printStackTrace();
		}


	}


	public static void getGoods(){

		try {
			InputStream is = new File("src/main/resources/Music/getGoods.wav").toURI().toURL().openStream();
			AudioPlayer.player.start(new AudioStream(is));//开始播放
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void putBubble(){

		try {
			InputStream is = new File("src/main/resources/Music/putBubble.wav").toURI().toURL().openStream();
			AudioPlayer.player.start(new AudioStream(is));//开始播放
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void bubbleBomb(){

		try {
			InputStream is = new File("src/main/resources/Music/bubbleBomb.wav").toURI().toURL().openStream();
			AudioPlayer.player.start(new AudioStream(is));//开始播放
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init(){
		System.out.println("声音控制初始化");
	}

}
