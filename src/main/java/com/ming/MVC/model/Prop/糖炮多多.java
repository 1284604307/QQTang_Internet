package com.ming.MVC.model.Prop;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.base.Point;
import javafx.scene.canvas.GraphicsContext;

public class 糖炮多多 extends Prop {

    {
        super.unitType = UnitType.糖炮多多;
    }

    @Deprecated
    public 糖炮多多(int px, int py){
        super(px,py);
    }

    public 糖炮多多(Point point){
        super(point);
    }
    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.道具1,position.x+5,position.y+2,40,40);
    }

    @Override
    public void update() {

    }
}
