package org.ming.connect.model;

import org.ming.model.RoomList;

@FunctionalInterface
public interface RoomListListener {

    public void onChange(RoomList roomList);
}
