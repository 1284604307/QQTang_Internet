package org.ming.connect.model;

import org.ming.model.base.Point;
import org.ming.model.base.Position;
import org.ming.model.base.UnitType;

public class RecycleMark {
    private int y;
    private int x;
    private UnitClass unit ;
    private int dir ;

    public RecycleMark(int y,int x, UnitClass unitType) {
        this.y = y;
        this.x=x;
        this.unit = unitType;
    }

    public RecycleMark(int y,int x, UnitClass unitType,int dir) {
        this.y = y;
        this.x=x;
        this.unit = unitType;
        this.dir = dir;
    }

    public int getDir() {
        return dir;
    }

    public void setDir(int dir) {
        this.dir = dir;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public UnitClass getUnit() {
        return unit;
    }

    public void setUnit(UnitClass unit) {
        this.unit = unit;
    }

    @Override
    public String toString() {
        return "RecycleMark{" +
                "y=" + y +
                ", x=" + x +
                ", unit=" + unit +
                ", dir=" + dir +
                '}';
    }
}
