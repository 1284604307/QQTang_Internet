package com.ming;

import com.ming.MVC.model.Item;

import java.util.ArrayList;

public class GameObjectManager {

    private static int rows = 13;
    private static int cols = 15;
    private Item[][] items = new Item[rows][cols];
    private ArrayList<Item>[][] itemsArr = new ArrayList[rows][cols];


    public void updateItems(ArrayList<Item>[][] items){
        this.itemsArr = items;
    }

    public void updateItems(){
        items = new Item[rows][cols];
    }

    public void updateItemArrs(){itemsArr = new ArrayList[rows][cols];}

    public boolean hasObject(int row, int col){
        return itemsArr[row][col] != null;
    }

    public ArrayList<Item> getItemArrList(int col, int row){return itemsArr[row][col];}

    public Item[][] getItems() {
        return items;
    }

    public ArrayList<Item>[][] getItemsArr() {
        return itemsArr;
    }

    public boolean hasItem(Item node){
        int row = node.getPoint().y;
        int col = node.getPoint().x;

        if (GameObjectManager.rows< row ||GameObjectManager.cols< col )
            throw new RuntimeException("Item 位置错误");

        if (null == itemsArr[row][col]) {
            itemsArr[row][col] = new ArrayList<>();
            return false;
        }
        if (itemsArr[row][col].contains(node))
            return true;
        return false;

    }

    public void addNode(Item node){
        int row = node.getPoint().y;
        int col = node.getPoint().x;
        if (GameObjectManager.rows< row ||GameObjectManager.cols< col ) throw new RuntimeException("Item 位置错误");
        if (null == itemsArr[row][col]) itemsArr[row][col] = new ArrayList<>();

        for (int i = 0; i < itemsArr[row][col].size(); i++) {
            ArrayList<Item> items = itemsArr[row][col];
            if (node == items.get(i)) return;
        }
//        items[row][col] = node;
        itemsArr[row][col].add(node);
        System.out.println("成功加入一个节点 "+node);
    }

    public void deleteNode(Item node){

        int row = node.getPoint().y;
        int col = node.getPoint().x;
        if (GameObjectManager.rows<row||GameObjectManager.cols<col) throw new RuntimeException("Item 位置错误");
        itemsArr[row][col].remove(node);
    }
}
