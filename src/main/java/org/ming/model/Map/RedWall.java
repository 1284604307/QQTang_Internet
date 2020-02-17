package org.ming.model.Map;

import org.ming.model.World;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;

public class RedWall extends Wall {


    {
        super.unitType = UnitType.RED_WALL;
        super.transX = 0;
        super.transY = -10;
    }

    public RedWall(int pointX , int pointY){

        super(pointX,pointY);
        this.point = new Point(pointX,pointY);
    }

//    @Override
//    public void draw(GraphicsContext g) {
//        if (dead){
//            drawDead(g);
//        }else
//            g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40,this.point.y * 40-10);
//    }

    @Override
    public void update(World world) {
        super.update(world);
    }

}
