package org.ming.mapEditer;

import io.vertx.core.json.JsonObject;
import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.ming.framework.Navigator;
import org.ming.framework.PushAndPopPane;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.UnitType;

import java.io.*;

public class MapEditor extends PushAndPopPane {
    private static GraphicsContext g ;
    private static UnitType[][] maps = new UnitType[13][15];
    private int x;
    private int y;
    private UnitType unitType = UnitType.WALL;

    public MapEditor(Stage stage) {
        this(stage,new JsonObject());
    }

    public MapEditor(Stage stage, JsonObject jsonObject) {
        super(stage, jsonObject);
        stage.setTitle("地图编辑器 ， 左键布置，右键删除 ");
        AnchorPane root = new AnchorPane();
        this.getChildren().add(root);
        Canvas canvas = new Canvas(600,520);
        root.getChildren().add(canvas);
        g = canvas.getGraphicsContext2D();



        canvas.setOnMouseMoved(event -> {
            x = (int)event.getX()/40;
            y = (int)event.getY()/40;
        });

        canvas.setOnMouseClicked(event ->  {
            if (event.getButton()== MouseButton.PRIMARY) {
                maps[y][x] = unitType;
            }else {
                maps[y][x] = UnitType.NULL;
            }
        });

        canvas.setFocusTraversable(true);

        GridPane gridPane = new GridPane();
        root.getChildren().add(gridPane);

        gridPane.setLayoutX(600);
        gridPane.setHgap(5);
        root.setStyle("-fx-background-color: #00000088");

        ImageView wall = new ImageView(ImageUrl.Wall);
        wall.setOnMouseClicked(event -> {
            this.unitType = UnitType.WALL;
        });

        ImageView redWall = new ImageView(ImageUrl.RedWall);
        redWall.setOnMouseClicked(event -> {
            this.unitType = UnitType.RED_WALL;
        });

        {
            gridPane.add(wall,0,0);
            gridPane.add(redWall,1,0);
            gridPane.add(new UnitImage(ImageUrl.水1,UnitType.水1),2,0);
            gridPane.add(new UnitImage(ImageUrl.水2,UnitType.水2),3,0);
            gridPane.add(new UnitImage(ImageUrl.水3,UnitType.水3),4,0);

            gridPane.add(new UnitImage(ImageUrl.水4,UnitType.水4),0,1);
            gridPane.add(new UnitImage(ImageUrl.花瓶,UnitType.FLOWER_PORT),1,1);
            gridPane.add(new UnitImage(ImageUrl.比,UnitType.比),2,1);
            gridPane.add(new UnitImage(ImageUrl.武,UnitType.武),3,1);
            gridPane.add(new UnitImage(ImageUrl.Cactus,UnitType.CACTUS),4,1);
            gridPane.add(new UnitImage(ImageUrl.冰墙,UnitType.冰墙),0,2);
            gridPane.add(new UnitImage(ImageUrl.沙墙,UnitType.沙墙),1,2);
            gridPane.add(new UnitImage(ImageUrl.沙,UnitType.沙),2,2);
            gridPane.add(new UnitImage(ImageUrl.铁块,UnitType.IRON_WALL),3,2);
            gridPane.add(new UnitImage(ImageUrl.桶,UnitType.桶),4,2);
        }

        Button save = new Button("保存地图");
        save.setOnMouseClicked(event -> {
            save(stage);
        });
        save.setLayoutX(700);
        save.setLayoutY(400);
        Button read = new Button("读取地图");
        read.setOnMouseClicked(event -> {
            read(stage);
        });

        Button loadOut = new Button("返回");
        loadOut.setOnMouseClicked(event -> {
            Navigator.pop();
            Navigator.pop(jsonObject);
        });

        read.setLayoutX(700);
        read.setLayoutY(450);

        loadOut.setLayoutX(700);
        loadOut.setLayoutY(350);

        root.getChildren().add(save);
        root.getChildren().add(read);
        root.getChildren().add(loadOut);
        stage.show();
        new aT().start();
    }

    private void read(Stage stage){
        FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例
        fileChooser.setInitialDirectory(new File("src"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map Files", "*.map"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        try {
            File file = selectedFile.getAbsoluteFile();
            System.out.println(file.getName());

            FileReader reader = new FileReader(file);
            BufferedReader bReader = new BufferedReader(reader);//new一个BufferedReader对象，将文件内容读取到缓存
            StringBuilder sb = new StringBuilder();//定义一个字符串缓存，将字符串存放缓存中
            String s = "";
            while ((s =bReader.readLine()) != null) {//逐行读取文件内容，不读取换行符和末尾的空格
                sb.append(s).append("\n");//将读取的字符串添加换行符后累加存放在缓存中
            }
            bReader.close();
            String str = sb.toString();
            System.out.println(str);
            String[] unitTypes = str.split("\t");
            int Y = Integer.parseInt(unitTypes[0]);
            int X= Integer.parseInt(unitTypes[1]);
            maps = new UnitType[Y][X];

            for (int y=0;y<maps.length;y++) {
                for (int x=0;x<maps[y].length;x++) {
                    maps[y][x] = UnitType.valueOf(unitTypes[X*y+x+2]);
                }
            }
        }catch (Exception ignored) { }



    }

    private void save(Stage stage) {

//        out.println(UnitType.valueOf("WALL"));




        FileChooser fileChooser = new FileChooser();//构建一个文件选择器实例
        fileChooser.setInitialDirectory(new File("src"));
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("Map Files", "*.map","*.txt"));
        File file = fileChooser.showOpenDialog(stage);


//        if(!file.exists()) {
//            try {
//                file.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            System.out.println("创建文件成功");
//        }

        FileWriter out = null;
        try {


            out = new FileWriter(file);

            out.write(maps.length+"\t"+maps[0].length+"\t");

            for (int y=0;y<maps.length;y++) {
                for (int x=0;x<maps[y].length;x++) {
                    if (maps[y][x]==null)
                        out.write(UnitType.NULL+"\t");
                    else out.write(maps[y][x]+"\t");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (out!=null)
                    out.close();
            }catch (Exception ignored){}
        }
    }

    class UnitImage extends ImageView{
        UnitImage(Image image,UnitType uType){
            super(image);
            super.setOnMouseClicked(event -> {
                unitType = uType;
            });
        }
    }

    class aT extends AnimationTimer{

        @Override
        public void handle(long now) {


            g.drawImage(ImageUrl.stand,0,0);
            for (int y=0;y<maps.length;y++) {
                for (int x=0;x<maps[y].length;x++) {
                    if (maps[y][x] != null)
                        g.drawImage(ImageUrl.maps.get(maps[y][x]),x*40-1,y*40-10,42,54);
                }
            }
            if (unitType!=null){
                g.setGlobalAlpha(0.6);
                g.drawImage(ImageUrl.maps.get(unitType),x*40-1,y*40-10,42,54);
                g.setGlobalAlpha(1);
            }
            try {
                Thread.sleep(33);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
