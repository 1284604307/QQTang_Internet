package org.ming.model.prop;

import javafx.scene.canvas.GraphicsContext;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Point;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.GameObject;

import java.util.ArrayList;

public class Prop extends GameObject {

    public Prop(){}

    public Prop(Point point){

        super(point);
        this.point = point;
        this.setPosition(new Position(point.x*40,point.y*40));
    }

    public static Prop init(ArrayList<String> item){
        //ex  [Wall,x,y,pX,pY,dead,recycleD,transX,transY]
        Prop prop = new Prop(Integer.parseInt(item.get(3)), Integer.parseInt(item.get(4)));
        prop.unitType = UnitType.valueOf(item.get(0));
        return prop;

    }

    @Deprecated
    public Prop(int px,int py){
        super(px,py);
        this.setPosition(new Position(point.x*40,point.y*40));
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(unitType),position.x+5,position.y+2,30,35);
    }

    @Override
    public void update(World world) {

    }
}
