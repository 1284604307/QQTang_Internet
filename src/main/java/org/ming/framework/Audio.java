package org.ming.framework;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;

import java.io.File;
import java.net.URL;
import java.util.*;

public class Audio {
    private Map<String, Object> loadedFiles = new HashMap();
    {
        URL bubbleBomb_url = null;
        URL putBubble_url = null;
        URL getGoods_url = null;
        URL BGM_url = null;
            try {
            bubbleBomb_url = new File("src/main/resources/Music/bubbleBomb.wav").toURI().toURL();
            putBubble_url = new File("src/main/resources/Music/putBubble.wav").toURI().toURL();
            getGoods_url = new File("src/main/resources/Music/getGoods.wav").toURI().toURL();
            BGM_url = new File("src/main/resources/Music/bgm.wav").toURI().toURL();
        } catch (Exception ignored){}
    }

    public Audio() {
    }

    public void remove(String fileName) {
        this.loadedFiles.remove(fileName);
    }

    public void clearCache() {
        this.loadedFiles.clear();
    }

    public List<Object> loadAll(String ...fileNames) {
        ArrayList<Object> list = new ArrayList<>();
//        Iterator var3 = fileNames.iterator();
//
//        while(var3.hasNext()) {
//            String fileName = (String)var3.next();
//            list.add(this.load(fileName));
//        }

        for (String fileName : fileNames) {
            list.add(this.load(fileName));
        }

        return list;
    }

    public Object load(String fileName) {
        if (!this.loadedFiles.containsKey(fileName)) {
            this.loadedFiles.put(fileName, this.fetchToMemory(fileName));
        }

        return this.loadedFiles.get(fileName);
    }

    private Object fetchToMemory(String name) {
        return name.endsWith("mp3") ? new Media(this.getClass().getResource("/audio/" + name).toExternalForm()) : new AudioClip(this.getClass().getResource("/Music/" + name).toExternalForm());
    }

    public Object get(String fileName) {
        if (!this.loadedFiles.containsKey(fileName) || this.loadedFiles.get(fileName) == null) {
            System.out.println("file: Music/" + fileName + " doesn't exist reload");
            this.loadedFiles.put(fileName, this.fetchToMemory(fileName));
        }

        return this.loadedFiles.get(fileName);
    }

    public void put(String key, Object audio) {
        this.loadedFiles.put(key, audio);
    }

    public int size() {
        return this.loadedFiles.size();
    }
}