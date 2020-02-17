package org.ming.model.prop;

import javafx.scene.canvas.GraphicsContext;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;

public class 威力嘎嘎 extends Prop {

    {
        super.unitType = UnitType.威力嘎嘎;
    }

    public 威力嘎嘎(Point point){
        super(point);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(unitType),position.x+5,position.y+2,30,35);
    }

}
