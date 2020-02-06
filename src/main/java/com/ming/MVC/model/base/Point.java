package com.ming.MVC.model.base;

public class Point {
    public int x;
    public int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

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
}
