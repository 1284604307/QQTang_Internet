package org.ming.model;

import org.ming.model.Map.Wall;
import org.ming.model.baseInterface.GameObject;
import org.ming.model.bubble.Bubble;
import org.ming.model.exploding.Exploding;
import org.ming.model.prop.Prop;

import java.util.ArrayList;

public class GameObjectManager {

    private int rows = 13;
    private int cols = 15;
    private Wall[][] walls = new Wall[rows][cols];
    private Bubble[][] bubbles = new Bubble[rows][cols];
    private Prop[][] props = new Prop[rows][cols];
    private Exploding[][] explodings = new Exploding[rows][cols];

    public Wall[][] getWalls() {
        return walls;
    }

    public void setWalls(Wall[][] walls) {
        this.walls = walls;
    }

    public Bubble[][] getBubbles() {
        return bubbles;
    }

    public void setBubbles(Bubble[][] bubbles) {
        this.bubbles = bubbles;
    }

    public Prop[][] getProps() {
        return props;
    }

    public void setProps(Prop[][] props) {
        this.props = props;
    }

    public Exploding[][] getExplodings() {
        return explodings;
    }

    public void setExplodings(Exploding[][] explodings) {
        this.explodings = explodings;
    }

    public int getRows() {
        return rows;
    }

    public void setRows(int rows) {
        this.rows = rows;
    }

    public int getCols() {
        return cols;
    }

    public void setCols(int cols) {
        this.cols = cols;
    }
}
