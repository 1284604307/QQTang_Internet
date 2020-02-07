package com.ming;

import com.ming.MVC.model.Bubble;
import com.ming.MVC.model.Exploding;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.Map.IronWall;
import com.ming.MVC.model.Map.RedWall;
import com.ming.MVC.model.Map.Wall;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;

import java.util.ArrayList;

import static com.ming.MVC.model.UnitType.FLOWER_PORT;

public class LocalGameObjectManager {

    private ArrayList<Item> items = new ArrayList<>();


    public void updateItems(ArrayList<ArrayList<String>> arrayLists){
        ArrayList<Item> items = new ArrayList<>();
        for (ArrayList<String> item:arrayLists) {
            UnitType unitType = UnitType.valueOf(item.get(0));
            if (unitType == UnitType.BUBBLE){
                Bubble bubble = new Bubble();
                bubble.position = new Position(Double.valueOf(item.get(1)),Double.valueOf(item.get(2)));
                bubble.point = new Point(Integer.parseInt(item.get(3)),Integer.parseInt(item.get(4)));
                bubble.power = Integer.parseInt(item.get(7));
                bubble.playerId = Integer.parseInt(item.get(8));
                items.add(bubble);
            }else if (unitType == UnitType.EXPLODING){
                Exploding exploding = new Exploding(new Point(Integer.parseInt(item.get(3)),Integer.parseInt(item.get(4))),Byte.parseByte(item.get(7)));
                items.add(exploding);
            }
            switch (unitType){
                case WALL:
                case RED_WALL:
                    items.add(Wall.init(item));
                    break;
                case IRON_WALL:
                case 仙人掌:
                case FLOWER_PORT:
                    items.add(IronWall.init(item));
                    break;
            }
        }
        this.items = items;
    }

    public ArrayList<Item> getItems() {
        return items;
    }

    public void addNode(Item node){
        items.add(node);
    }

    public void remvoe(Item node){
        items.remove(node);
    }
}
