package org.ming.controller;

import javafx.scene.input.KeyCode;

import java.util.Vector;

public class KeyStack<T>{

    private Vector<T> stack;
    private int curLen = -1;
    public KeyStack() {
        stack = new Vector<T>();
    }

    public int count(){
        return curLen;
    }

    public void push(T item) {
        if (stack.contains(item)) return;
        stack.add(item);
        curLen++;
    }

    public void remove(T t){
        curLen--;
        stack.remove(t);
    }

    public T remove(int i){
        T t = stack.get(i);
        curLen--;
        stack.remove(i);
        return t;
    }

    public synchronized T pop() {
        T t = stack.get(curLen);
        stack.remove(curLen--);
        return t;
    }

    public synchronized T peek() {
        if (curLen == -1)
            throw new RuntimeException("键栈为空");
        return stack.get(curLen);
    }

    public boolean isEmpty(){
        return curLen < 0;
    }

    public void empty() {
        curLen=-1;
        stack.clear();
    }

    public synchronized int search(T o) {
        for (int i = 0; i < curLen+1; i++) {
            if (o == stack.get(i)) return i;
        }
        return -1;
    }
}
