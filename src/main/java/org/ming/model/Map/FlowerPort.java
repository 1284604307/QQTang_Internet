package org.ming.model.Map;


import org.ming.model.World;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.BlockBox;

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
    public void update(World world) {

    }
}
