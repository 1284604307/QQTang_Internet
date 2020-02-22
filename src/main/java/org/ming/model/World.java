package org.ming.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ming.framework.MusicManager;
import org.ming.model.Map.IronWall;
import org.ming.model.Map.Wall;
import org.ming.model.base.BaseData;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.GameObject;
import org.ming.model.bubble.Bubble;
import org.ming.model.exploding.Exploding;
import org.ming.model.players.Player;
import org.ming.model.prop.Prop;

import java.io.*;

public class World {

    private GameObjectManager gameObjectManager = new GameObjectManager();
    private Player[] players ;
    private MusicManager musicManager = MusicManager.getManager();

    public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public GameObjectManager getGameObjectManager() {
        return gameObjectManager;
    }

    public void setGameObjectManager(GameObjectManager gameObjectManager) {
        this.gameObjectManager = gameObjectManager;
    }

    public void loadMap(File file) throws IOException {
        if (file==null)
            file = new File("src\\比武大地图.map");
        FileReader reader = new FileReader(file);
        BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
        StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
        String s = "";
        while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
            sb.append(s + "\n");//将读取的字符串添加换行符后累加存放在缓存中
        }
        bReader.close();
        String str = sb.toString();
        System.out.println(str);
        String[] unitTypes = str.split("\t");
        int Y = Integer.parseInt(unitTypes[0]);
        int X= Integer.parseInt(unitTypes[1]);
        UnitType[][] maps = new UnitType[Y][X];

        for (int y=0;y<maps.length;y++) {
            for (int x=0;x<maps[y].length;x++) {
                UnitType unitType = UnitType.valueOf(unitTypes[X * y + x + 2]);
                maps[y][x] = unitType;
                switch (unitType){
                    case 比:
                    case 武:
                        Wall wall = new Wall(x, y);
                        wall.setUnitType(unitType);
                        gameObjectManager.getWalls()[y][x] = wall;
                        break;
                    case 桶:
                        IronWall ironWall = new IronWall(x, y);
                        ironWall.setUnitType(unitType);
                        gameObjectManager.getWalls()[y][x] = ironWall;
                        break;
                }
            }
        }
    }

    public void read(Stage stage ){
        FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例
        fileChooser.setInitialDirectory(new File("src"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map Files", "*.map"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        System.out.println(selectedFile);
        try {
            loadMap(selectedFile);
        }catch (Exception ignored) { }
    }

    public boolean hasBubble(int y, int x){
        if (notIndexOut(y,x))
            if (gameObjectManager.getBubbles()[y][x]!=null)
                return true;
        return false;
    }

    public void putBubble(int y,int x,Bubble bubble){
        if (!hasBubble(y,x)) {
            musicManager.putBubble();
            gameObjectManager.getBubbles()[y][x] = bubble;
            System.out.println(hasBubble(y,x));
        }
    }

    public MusicManager getMusicManager() {
        return musicManager;
    }

    public void setMusicManager(MusicManager musicManager) {
        this.musicManager = musicManager;
    }

    public boolean hasWall(int y, int x){

        if (notIndexOut(y,x))
            return gameObjectManager.getWalls()[y][x] != null;
        return false;
    }

    public boolean hasProp(int y,int x){
        if (notIndexOut(y,x))
            return gameObjectManager.getProps()[y][x] != null;
        return false;
    }

    private boolean notIndexOut(int y, int x) {
        return y<getRows()&&x<getCols();
    }

    public boolean hasExploding(int y,int x){

        if (notIndexOut(y,x))
            return gameObjectManager.getExplodings()[y][x] != null;
        return false;
    }

    public Exploding getExploding(int y, int x){
        return gameObjectManager.getExplodings()[y][x];
    }

    public void draw(GraphicsContext context, int y, int x){
        if (hasProp(y,x))
            getProp(y, x).draw(context);
        if (hasWall(y,x))
            getWall(y, x).draw(context);
        if (hasBubble(y,x))
            getBubble(y, x).draw(context);
        if (hasExploding(y,x))
            getExploding(y, x).draw(context);
    }

    public void update(int y,int x){
        if (hasProp(y,x)){
            getProp(y, x).update(this);
            if (getProp(y,x).recycled()){
                gameObjectManager.getProps()[y][x]=null;
            }
        }
        if (hasWall(y,x)){
            getWall(y, x).update(this);
            if (getWall(y,x).recycled()){
                GameObject dead = getWall(y, x).dead();
                gameObjectManager.getWalls()[y][x]=null;
                if (dead!=null)
                addNode((Prop)dead);
            }
        }
        if (hasBubble(y,x)){
            Bubble bubble = getBubble(y, x);
            bubble.update(this);
            if (bubble.recycled()){
                gameObjectManager.getBubbles()[y][x]=null;
                openExplore(bubble);
            }
        }
        if (hasExploding(y,x)){
            getExploding(y,x).update(this);
            if (getExploding(y,x).recycled()){
                gameObjectManager.getExplodings()[y][x]=null;
            }
        }
    }

    public void addNode(Prop gameObject){
        gameObjectManager.getProps()[gameObject.getPoint().y][gameObject.getPoint().x] = gameObject;

    }

    public void addNode(Bubble gameObject){

    }

    public void addNode(Wall gameObject){
        gameObjectManager.getWalls()[gameObject.getPoint().y][gameObject.getPoint().x] = gameObject;
    }

    public void addNode(Exploding gameObject){
        gameObjectManager.getExplodings()[gameObject.getPoint().y][gameObject.getPoint().x] = gameObject;
    }

    public void removeNode(Prop object){

        gameObjectManager.getProps()[object.getPoint().y][object.getPoint().x]=null;
    }
    public void removeNode(Bubble object){

        gameObjectManager.getWalls()[object.getPoint().y][object.getPoint().x]=null;
    }
    public void removeNode(Wall object){

        gameObjectManager.getBubbles()[object.getPoint().y][object.getPoint().x]=null;
    }
    public void removeNode(Exploding object){

        gameObjectManager.getExplodings()[object.getPoint().y][object.getPoint().x]=null;
    }

    private void openExplore(Bubble item) {

        if(item.isBoom()) return;
        item.boom();
        musicManager.bubbleBomb();
        Point point = item.getPoint();
        removeNode(item);

        // todo 爆炸初始化
        Exploding exploding = new Exploding(point);
        addNode(exploding);

//        serverHelper.sendAll(new DoMessage(BUBBLE_BOOM,""));

        boolean goL=true,
                goR=true,
                goUp=true,
                goDown=true;

        //todo
        // i 从 0开始，被我忽视了
        for (int i = 1; i <= item.getPower(); i++) {

            int pX = point.x;
            int pY = point.y;
            if (pX- i <0) goL=false;
            if (pX+ i >=15) goR=false;
            if (pY- i <0) goUp=false;
            if (pY+ i >=13) goDown = false;

            if (goL){
                goL = justJudge( pX- i, pY , i ==item.getPower()? BaseData.LEFT:BaseData.CENTER);
            }
            if (goR){
                goR = justJudge(pX+ i, pY , i ==item.getPower()? BaseData.RIGHT:BaseData.CENTER);
            }
            if (goUp){
                goUp = justJudge( pX, pY- i, i ==item.getPower()? BaseData.UP:BaseData.CENTER);
            }
            if (goDown){
                goDown = justJudge(pX, pY+ i, i ==item.getPower()? BaseData.DOWN:BaseData.CENTER);
            }

        }
    }

    /**
     * 判断当前延伸方向是否有节点，节点类型及其触发手段
     * @return 返回true 可继续延伸 false 不可继续
     */
    private boolean justJudge(int x,int y,byte dir){

        if (hasWall(y,x)){
            GameObject dead = getWall(y, x).dead();
            if (dead instanceof Prop) {
                addNode((Prop) dead);
            }
            return false;
        }
        if (hasBubble(y,x)){
            getBubble(y,x).dead();
            openExplore(getBubble(y,x));
            return false;
        }
        if (hasProp(y,x)){
            getProp(y,x).recycle();
        }
        Exploding exploding = new Exploding(new Point(x, y), dir);
        addNode(exploding);
        return true;
    }


    public Prop getProp(int y,int x){
        return gameObjectManager.getProps()[y][x];
    }

    public Bubble getBubble(int y, int x){
        return gameObjectManager.getBubbles()[y][x];
    }

    public Wall getWall(int y, int x){
        return gameObjectManager.getWalls()[y][x];
    }

    public boolean hasItem(int y,int x){
        return hasBubble(y,x)||hasWall(y,x)||hasProp(y,x);
    }


    public int getRows() {
        return gameObjectManager.getRows();
    }

    public int getCols() {
        return gameObjectManager.getCols();
    }
}
