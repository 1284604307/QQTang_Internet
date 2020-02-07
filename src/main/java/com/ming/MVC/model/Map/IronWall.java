package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

public class IronWall extends Wall {

    {
        super.unitType = UnitType.IRON_WALL;
        super.transX = -2;
        super.transY = -9;
    }

    public IronWall(Point point){

        super(point);
    }



    public IronWall(int px,int py){
        super(px,py);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40+transX,this.point.y * 40+transY);
    }

    @Override
    public void update() {

    }

}
