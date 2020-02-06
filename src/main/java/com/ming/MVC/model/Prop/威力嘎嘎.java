package com.ming.MVC.model.Prop;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.base.Point;
import javafx.scene.canvas.GraphicsContext;

public class 威力嘎嘎 extends Prop {

    {
        super.unitType = UnitType.威力嘎嘎;
    }

    public 威力嘎嘎(Point point){
        super(point);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.道具3,position.x+5,position.y+2,30,35);
    }

    @Override
    public void update() {

    }
}
