package com.ming.MVC.model.Map;

import com.ming.MVC.model.ImageUrl;
import com.ming.MVC.model.UnitType;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.Item;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.baseInterface.BlockBox;

public class 仙人掌 extends IronWall  implements BlockBox {

    {
        super.unitType = UnitType.CACTUS;
        super.transX = -2;
        super.transY = -9;
    }

    public 仙人掌(Point point){

        super(point);
    }
    @Deprecated
    public 仙人掌(int px,int py){
        super(px,py);
    }

    @Override
    public void update() {

    }
}
