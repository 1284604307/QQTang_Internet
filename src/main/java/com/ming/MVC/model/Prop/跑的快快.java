package com.ming.MVC.model.Prop;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.base.Point;
import javafx.scene.canvas.GraphicsContext;

public class 跑的快快 extends Prop {

    {
        super.unitType = UnitType.跑的快快;
    }

    public 跑的快快(Point point){
        super(point);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.道具2,position.x+5,position.y+2,40,40);
    }

    @Override
    public void update() {

    }
}
