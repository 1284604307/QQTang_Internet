package com.ming.MVC.model;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import com.ming.MVC.model.base.Point;
import com.ming.MVC.model.base.Position;

public abstract class Item {
    double width = 40;
    double height = 40;
    // 绘图审查的基础位置
//    @Deprecated
//    public int x = 0;
//    @Deprecated
//    public int y = 0;
    // 贴图
    protected Image image;
    // 毁灭标记
    protected boolean dead = false;
    // 回收标记
    protected boolean recycled = false;
    protected UnitType unitType = UnitType.NULL;
    // 地图数组上的点坐标
    protected Point point;
    // 真实坐标
    public Position position;


    public Item(){

        this.point =new Point(0,0);
        this.position =new Position(0,0);
    }

    public Item(Point point) {
        this.point =point;
        this.position= new Position(point.x*40,point.y*40);
    }

    public Item(int pX,int pY) {
        this.point =new Point(pX,pY);
        this.position= new Position(point.x*40,point.y*40);
    }



    public Item dead(){this.dead = true; return null;}

    public void recycle(){this.recycled = true; }

    public void setPosition(Position position){
        this.position = new Position(position.x,position.y);
    }

    public Point getPoint() {
        return point;
    }

    public UnitType getUnitType(){return unitType;}

    public abstract void draw(GraphicsContext g);

    public abstract void update();

    public boolean recycled(){ return  recycled;}


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Item) {
            Point p = ((Item)obj).point;
            return p.x==point.x&&p.y==point.y&&getUnitType()==((Item) obj).unitType;
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
