package org.ming.model.baseInterface;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import org.ming.model.World;
import org.ming.model.base.Point;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;

import static org.ming.model.base.BaseData.CENTER;

public abstract class GameObject {
    // 毁灭标记
    protected boolean dead = false;
    // 回收标记
    protected boolean recycled = false;
    protected UnitType unitType = UnitType.NULL;
    // 地图数组上的点坐标
    protected Point point;
    // 真实坐标
    public Position position;
    protected byte dir = CENTER;
    protected Image image;

    public GameObject(){

        this.point =new Point(0,0);
        this.position =new Position(0,0);
    }

    public GameObject(Point point) {
        this.point =point;
        this.position= new Position(point.x*40,point.y*40);
    }

    public GameObject(int pX, int pY) {
        this.point =new Point(pX,pY);
        this.position= new Position(point.x*40,point.y*40);
    }

    public byte getDir() {
        return dir;
    }

    public void setDir(byte dir) {
        this.dir = dir;
    }

    public GameObject dead(){this.dead = true; return null;}
    public boolean isDie(){return dead;}

    public void recycle(){this.recycled = true; }

    public double getX() {
        return position.x;
    }
    public double getY() {
        return position.y;
    }
    public void setPosition(Position position){
        this.position = new Position(position.x,position.y);
    }

    public Point getPoint() {
        return point;
    }

    public void setUnitType(UnitType unitType) {
        this.unitType = unitType;
    }

    public UnitType getUnitType(){return unitType;}

    public abstract void draw(GraphicsContext g);

    public void update(World world){};

    public boolean recycled(){ return  recycled;}


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof GameObject) {
            Point p = ((GameObject)obj).point;
            return p.x==point.x&&p.y==point.y;
        }else return false;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"dead\":")
                .append(dead);
        sb.append(",\"recycled\":")
                .append(recycled);
        sb.append(",\"unitType\":")
                .append(unitType);
        sb.append(",\"point\":")
                .append(point);
        sb.append(",\"position\":")
                .append(position);
        sb.append('}');
        return sb.toString();
    }
}
