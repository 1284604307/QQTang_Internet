package com.ming.MVC.model;

import com.ming.MVC.model.data.BaseData;
import javafx.scene.canvas.GraphicsContext;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;

import static com.ming.MVC.model.data.BaseData.*;

/**
 * 爆炸类
 */
public class Exploding extends Item {

    private int deadTimer = 0;
    private byte dir = CENTER;
    {
        this.unitType = UnitType.EXPLODING;
    }

    @Deprecated
    public Exploding(Position centerPositon){
        this.position = centerPositon;
    }

    public Exploding(Point point){

        this.point = point;
        this.position = new Position(point.x*40,point.y*40);
    }

    public Exploding(Point point,byte dir){

        this.point = point;
        this.dir = dir;
        this.position = new Position(point.x*40,point.y*40);
    }

    private void recycled(boolean isD){
        recycled = isD;
    }

    public boolean recycled(){
        return recycled;
    }

    @Override
    public void draw(GraphicsContext g) {

        if (!recycled)
            switch (dir){
                case CENTER:
                    g.drawImage(ImageUrl.expCenter,position.x,position.y);
                    break;
                case LEFT:
                    g.drawImage(ImageUrl.expLeftTop,position.x,position.y);
                    break;
                case RIGHT:
                    g.drawImage(ImageUrl.expRightTop,position.x,position.y);
                    break;
                case UP:
                    g.drawImage(ImageUrl.expUpTop,position.x,position.y);
                    break;
                case DOWN:
                    g.drawImage(ImageUrl.expDownTop,position.x,position.y);
                    break;
            }

    }

    @Override
    public void update() {
        deadTimer+=20;

        if (deadTimer>200)
            recycled = true;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"dir\":")
                .append(dir);
        sb.append(",\"point\":")
                .append(point);
        sb.append(",\"position\":")
                .append(position);
        sb.append('}');
        return sb.toString();
    }
}
