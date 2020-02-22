package org.ming.model;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import org.ming.connect.model.Room;
import org.ming.connect.model.RoomListListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Observable;
import java.util.Vector;

public class RoomList extends ArrayList<Room>  {

    private Vector<RoomListListener> vector = new Vector<>();

    @Override
    public Room get(int index) {
        return super.get(index);
    }

    public void addListener(RoomListListener listener){
        vector.add(listener);
    }

    public void removeListener(RoomListListener listener) {
        vector.remove(listener);
    }

    @Override
    public Room set(int index, Room element) {
        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }
        return super.set(index, element);
    }

    @Override
    public boolean add(Room room) {

        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }

        return super.add(room);
    }

    @Override
    public void add(int index, Room element) {

        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }

        super.add(index, element);
    }

    @Override
    public Room remove(int index) {
        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }
        return super.remove(index);
    }

    @Override
    public boolean remove(Object o) {
        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }
        return super.remove(o);
    }

    @Override
    public void clear() {
        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }
        super.clear();
    }

    @Override
    public boolean addAll(Collection<? extends Room> c) {
        if (super.addAll(c)) {
            for (RoomListListener roomListListener : vector) {
                roomListListener.onChange(this);
            }
            return true;
        }else
            return false;
    }


    @Override
    protected void removeRange(int fromIndex, int toIndex) {
        for (RoomListListener roomListListener : vector) {
            roomListListener.onChange(this);
        }
        super.removeRange(fromIndex, toIndex);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        if (super.removeAll(c)) {
            for (RoomListListener roomListListener : vector) {
                roomListListener.onChange(this);
            }
            return true;
        }else
            return false;
    }
}
