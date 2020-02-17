package org.ming.model.Map;

import javafx.scene.canvas.GraphicsContext;
import org.ming.model.World;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.baseInterface.GameObject;

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
        g.drawImage(ImageUrl.maps.get(this.unitType),this.point.x * 40+transX,this.point.y * 40+transY,42,52);
    }

    @Override
    public GameObject dead() {
        return null;
    }

    @Override
    public void update(World world) {

    }
}
