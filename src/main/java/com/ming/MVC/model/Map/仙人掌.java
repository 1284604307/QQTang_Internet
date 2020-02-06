package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

public class 仙人掌 extends Item  implements BlockBox {

    {
        super.unitType = UnitType.CACTUS;
    }

    public 仙人掌(Point point){

        super(point);
    }
    @Deprecated
    public 仙人掌(int px,int py){
        super(px,py);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40-2,this.point.y * 40-9);
    }

    @Override
    public void update() {

    }
}
