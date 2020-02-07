package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

public class FlowerPort extends IronWall  implements BlockBox {
    {
        super.unitType = UnitType.FLOWER_PORT;
        super.transX = -4;
        super.transY = -18;
    }

    public FlowerPort(Point point){

        super(point);
    }

    public FlowerPort(int px,int py){
        super(px,py);
    }

    @Override
    public void update() {

    }
}
