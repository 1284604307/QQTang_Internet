package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

public class FlowerPort extends Item  implements BlockBox {
    {
        super.unitType = UnitType.FLOWER_PORT;
    }

    public FlowerPort(Point point){

        super(point);
    }

    public FlowerPort(int px,int py){
        super(px,py);
    }


    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40-4,this.point.y * 40-18);
    }

    @Override
    public void update() {

    }

}
