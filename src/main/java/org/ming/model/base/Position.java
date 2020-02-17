package org.ming.model.base;

public class Position implements Comparable<Position> {
    public double x;
    public double y;

    public Position(Position p){
        this.x = p.x;
        this.y = p.y;
    }

    public Position(){
        this.x = 0;
        this.y = 0;
    }

    public Position(double x, double y){
        this.x = x;
        this.y = y;
    }

    public void update(double x,double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void updateX(double x){this.x = x;}
    public void updateY(double y){this.y = y;}

    public void translateX(double x){this.x += x;}
    public void translateY(double y){this.y += y;}

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"x\":")
                .append(x);
        sb.append(",\"y\":")
                .append(y);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int compareTo(Position position) {
        if (position.x>x-26&&position.x<x+26){
//            System.out.println(position.y+"  自己 -> "+y);
            if (position.y>y-20 && position.y<y+20){
                return 0;
            }return -1;
        }else return -1;
    }
}
