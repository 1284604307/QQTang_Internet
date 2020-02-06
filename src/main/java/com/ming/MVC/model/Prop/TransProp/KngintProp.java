package com.ming.MVC.model.Prop.TransProp;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.Prop.Prop;
import com.ming.MVC.model.UnitType;
import com.ming.MVC.model.base.Point;
import javafx.scene.canvas.GraphicsContext;

public class KngintProp extends Prop {

    {
        unitType = UnitType.KNIGHT_PROP;
    }


    public KngintProp(Point point){
        super(point);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.a骑士,position.x+5,position.y+2,30,35);
    }

    @Override
    public void update() {

    }
}
