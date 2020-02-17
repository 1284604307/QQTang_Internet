package org.ming.model.base;

import static org.ming.model.base.BaseData.CENTER;

public class TransBaseUnit {

    // 毁灭标记
    protected boolean dead = false;
    // 回收标记
    protected boolean recycled = false;
    protected UnitType unitType = UnitType.NULL;
    // 地图数组上的点坐标
    protected Point point;
    // 真实坐标
    public Position position;
    private byte dir = CENTER;




}
