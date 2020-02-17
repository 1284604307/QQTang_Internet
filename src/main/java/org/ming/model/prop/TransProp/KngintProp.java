package org.ming.model.prop.TransProp;

import javafx.scene.canvas.GraphicsContext;
import org.ming.model.base.ImageUrl;
import org.ming.model.base.Point;
import org.ming.model.base.UnitType;
import org.ming.model.prop.Prop;

public class KngintProp extends Prop {

    {
        unitType = UnitType.KNIGHT_PROP;
    }


    public KngintProp(Point point){
        super(point);
    }

    @Override
    public void draw(GraphicsContext g) {
        g.drawImage(ImageUrl.maps.get(unitType),position.x+5,position.y+2,30,35);
    }

}
