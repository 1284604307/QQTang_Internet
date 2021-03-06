package org.ming.model.base;

import javafx.scene.image.Image;
import org.ming.model.PlayerName;

import java.util.HashMap;

public class ImageUrl {

    public final static Image background = new Image("file:src/main/resources\\Images\\BackgroundImage\\GameWindow.png");
    public final static Image stand = new Image("file:src/main/resources\\Images\\Map\\标准.jpg");

    public final static Image arrows0 = new Image("file:src/main/resources\\Images\\players\\arrows1.png");
    public final static Image arrows1 = new Image("file:src/main/resources\\Images\\players\\arrows2.png");
    public final static Image arrows2 = new Image("file:src/main/resources\\Images\\players\\arrows3.png");

    public final static String Monkey_0 = "file:src/main/resources\\Images\\players\\悟空下.gif";
    public final static String Monkey_1 = "file:src/main/resources\\Images\\players\\悟空上.gif";
    public final static String Monkey_2 = "file:src/main/resources\\Images\\players\\悟空左.gif";
    public final static String Monkey_3 = "file:src/main/resources\\Images\\players\\悟空右.gif";
    public final static String Monkey0_1 = "file:src/main/resources\\Images\\孙悟空\\悟空正面1.png";
    public final static String Monkey1_1 = "file:src/main/resources\\Images\\孙悟空\\悟空后1.png";
    public final static String Monkey2_1 = "file:src/main/resources\\Images\\孙悟空\\悟空左1.png";
    public final static String Monkey3_1 = "file:src/main/resources\\Images\\孙悟空\\悟空右1.png";

    public final static Image bubble_red = new Image("file:src/main/resources/Images/Bubble/糖泡红.gif");

    public final static Image InvincibleZero = new Image("file:src/main/resources\\Images\\players\\无敌特效.png");

    public final static String knigint_0 = "file:src/main/resources\\Images\\players\\knigint_0.gif";
    public final static String knigint_1 = "file:src/main/resources\\Images\\players\\knigint_1.gif";
    public final static String knigint_2 = "file:src/main/resources\\Images\\players\\knigint_2.gif";
    public final static String knigint_3 = "file:src/main/resources\\Images\\players\\knigint_3.gif";

    public final static Image Wall = new Image("file:src/main/resources\\Images\\Brick\\原始砖块.png");
    public final static Image RedWall = new Image("file:src/main/resources\\Images\\Brick\\原始砖块红.png");
    public final static Image 比 = new Image("file:src/main/resources\\Images\\Brick\\比.png");
    public final static Image 武 = new Image("file:src/main/resources\\Images\\Brick\\武.png");

    public final static Image Wall_EXP1 = new Image("file:src/main/resources\\Images\\Brick\\砖块破碎1.png");
    public final static Image Wall_EXP2 = new Image("file:src/main/resources\\Images\\Brick\\砖块破碎2.png");
    public final static Image Wall_EXP3 = new Image("file:src/main/resources\\Images\\Brick\\砖块破碎3.png");

    public final static Image 花瓶 =  new Image("file:src/main/resources\\Images\\Decoration\\花瓶.png");
    public final static Image 铁块 =  new Image("file:src/main/resources\\Images\\Decoration\\铁块.png");
    public final static Image Cactus = new Image("file:src/main/resources\\Images\\Decoration\\Cactus.png)");
    public final static Image 水1 =  new Image("file:src/main/resources\\Images\\Decoration\\水1.png");
    public final static Image 水2=  new Image("file:src/main/resources\\Images\\Decoration\\水2.png");
    public final static Image 水3 =  new Image("file:src/main/resources\\Images\\Decoration\\水3.png");
    public final static Image 水4 =  new Image("file:src/main/resources\\Images\\Decoration\\水4.png");
    public final static Image 沙 =  new Image("file:src/main/resources\\Images\\Decoration\\沙.png");
    public final static Image 沙墙 =  new Image("file:src/main/resources\\Images\\Decoration\\沙墙.png");
    public final static Image 冰墙 =  new Image("file:src/main/resources\\Images\\Decoration\\冰墙.png");
    public final static Image 桶 =  new Image("file:src/main/resources\\Images\\Decoration\\桶.png");

    public final static Image 糖炮多多 = new Image("file:src/main/resources\\Images\\Goods\\糖包.gif");
    public final static Image 跑的快快 = new Image("file:src/main/resources\\Images\\Goods\\飞鞋.gif");
    public final static Image 威力嘎嘎 = new Image("file:src/main/resources\\Images\\Goods\\威力.gif");
    public final static Image a反身怪 = new Image("file:src/main/resources\\Images\\Goods\\变身道具_反身怪.gif");
    public final static Image a骑士 = new Image("file:src/main/resources\\Images\\Goods\\变身道具_骑士.gif");
    public final static Image 泡泡皇后 = new Image("file:src/main/resources\\Images\\Goods\\变身道具_炮王.gif");

//    public final static


    public final static HashMap<UnitType,Image> maps;
    public final static HashMap<Integer , Image> arrows;
    static {
        maps = new HashMap<>();
        maps.put(UnitType.CACTUS,Cactus);
        maps.put(UnitType.WALL,Wall);
        maps.put(UnitType.RED_WALL,RedWall);
        maps.put(UnitType.FLOWER_PORT,花瓶);
        maps.put(UnitType.IRON_WALL,铁块);
        maps.put(UnitType.糖炮多多,糖炮多多);
        maps.put(UnitType.跑的快快,跑的快快);
        maps.put(UnitType.威力嘎嘎,威力嘎嘎);
        maps.put(UnitType.REFLEXIVE_MOSTER_PROP,a反身怪);
        maps.put(UnitType.泡泡皇后,泡泡皇后);
        maps.put(UnitType.比,比);
        maps.put(UnitType.桶,桶);
        maps.put(UnitType.武,武);
        maps.put(UnitType.冰墙, 冰墙);
        maps.put(UnitType.沙墙, 沙墙);
        maps.put(UnitType.沙, 沙);
        maps.put(UnitType.水1, 水1);
        maps.put(UnitType.水2, 水2);
        maps.put(UnitType.水3, 水3);
        maps.put(UnitType.水4, 水4);
        maps.put(UnitType.KNIGHT_PROP, a骑士);

        arrows = new HashMap<>();
        arrows.put(0,arrows0);
        arrows.put(1,arrows1);
        arrows.put(2,arrows2);
    }

    public final static HashMap<PlayerName,Image> roles = new HashMap<>();
    static {
        roles.put(PlayerName.火影,new Image(Monkey0_1));
    }

    // 3
    public final static Image[] wall_exps = new Image[]{
            Wall_EXP1,
            Wall_EXP2,
            Wall_EXP3,
    };

    // 16
    public final static Image[] Monkey = new Image[]{
            new Image(Monkey_0),
            new Image(Monkey_1),
            new Image(Monkey_2),
            new Image(Monkey_3),
            new Image(Monkey0_1),
            new Image(Monkey1_1),
            new Image(Monkey2_1),
            new Image(Monkey3_1)
    };


    // 4
    public final static Image[] Kngint = new Image[]{
            new Image(knigint_0),
            new Image(knigint_1),
            new Image(knigint_2),
            new Image(knigint_3)
    };

    public final static Image expCenter = new Image("file:src/main/resources\\Images\\Exploding\\爆炸中.png");
    public final static Image expLeftTop = new Image("file:src/main/resources\\Images\\Exploding\\爆炸左顶点.png");
    public final static Image expRightTop = new Image("file:src/main/resources\\Images\\Exploding\\爆炸右顶点.png");
    public final static Image expUpTop = new Image("file:src/main/resources\\Images\\Exploding\\爆炸上顶点.png");
    public final static Image expDownTop = new Image("file:src/main/resources\\Images\\Exploding\\爆炸下顶点.png");
}
