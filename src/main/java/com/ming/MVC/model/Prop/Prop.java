package com.ming.MVC.model.Prop;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;
import javafx.scene.canvas.GraphicsContext;

public abstract class Prop extends Item {

    public Prop(){}

    public Prop(Point point){

        super(point);
        this.point = point;
        this.setPosition(new Position(point.x*40,point.y*40));
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
}
