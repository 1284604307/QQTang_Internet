package com.ming.MVC.model;

public enum  TransUnit {
    UNIT_TYPE,
    x,
    y,
    px,
    py,
    DEAD,
    RECYCLED;
    /**
     *  len == 9?
     *  [Bubble,x,y,pX,pY,dead,recycleD,power,playerID]
     *  len == 7?
     *  [Wall,x,y,pX,pY,dead,recycleD,transX,transY]
     *
     */
}
