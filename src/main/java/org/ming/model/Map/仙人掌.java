package org.ming.model.Map;

import org.ming.model.World;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.BlockBox;

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
    public void update(World world) {

    }
}
