package org.ming.framework;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.File;
import java.net.MalformedURLException;

public class MusicManager {
	private final static File _BGM=new File("src/main/resources/Music/比武背景.wav");
	private final static File _putBubble=new File("src/main/resources/Music/putBubble.wav");
	private final static File _bubbleBomb=new File("src/main/resources/Music/bubbleBomb.wav");
	private final static File _getGoods=new File("src/main/resources/Music/getGoods.wav");
	private final static File _help=new File("src/main/resources/Music/被救.wav");
	private final static File _helpSelf=new File("src/main/resources/Music/自救.wav");
	private final static File _kill=new File("src/main/resources/Music/死亡.wav");
	private final static File _win=new File("src/main/resources/Music/胜利.wav");
	private final static File _readyGo=new File("src/main/resources/Music/开始.wav");
//	private Media _BGM ;

	private AudioClip bubbleBomb ;
	private AudioClip putBubble ;
	private AudioClip getGoods ;
	public AudioClip help ;
	public AudioClip win ;
	public AudioClip kill ;
	public AudioClip readyGo;
	private Media BGM ;

	private MediaPlayer mediaPlayer ;
//	public static AudioStream bubbleBomb ;
//	public static AudioStream putBubble ;
//	public static AudioStream getGoods ;
//	public static AudioStream BGM ;

	private static MusicManager musicManager;
	private MediaPlayer mplayer;

	private MusicManager(){
		try {
			bubbleBomb = new AudioClip(_bubbleBomb.toURI().toURL().toExternalForm());
			putBubble = new AudioClip(_putBubble.toURI().toURL().toExternalForm());
			getGoods = new AudioClip(_getGoods.toURI().toURL().toExternalForm());
			help = new AudioClip(_help.toURI().toURL().toExternalForm());
			BGM = new Media(_BGM.toURI().toURL().toExternalForm());
			win = new AudioClip(_win.toURI().toURL().toExternalForm());
			BGM = new Media(_BGM.toURI().toURL().toExternalForm());
			kill = new AudioClip(_kill.toURI().toURL().toExternalForm());
			readyGo = new AudioClip(_readyGo.toURI().toURL().toExternalForm());
//			BGM = new AudioClip(_BGM.toURI().toURL().toExternalForm());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public static MusicManager getManager(){
		if (musicManager==null)
			musicManager = new MusicManager();

		return musicManager;
	}

	public void win(){
		win.setCycleCount(2);
		win.play();
	}

	public void getGoods(){

		getGoods.play();
	}

	public void putBubble(){

		putBubble.play();
	}

	public void bubbleBomb(){

		bubbleBomb.play();
	}

	public void oBGM(){
		mplayer = new MediaPlayer(BGM);
		mplayer.play();
	}

	public void stopBGM(){
		mplayer.stop();
	}

	public void init(){
		System.out.println("声音控制初始化");
	}

    public void readyGo() {
    }
}
