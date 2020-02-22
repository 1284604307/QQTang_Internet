package org.ming.connect.model;

import java.util.ArrayList;
import java.util.Map;


public class HomeInfo {
    private String name;
    private String index;
    private ArrayList<User> users;

    public HomeInfo(String name, String index, ArrayList<User> users) {
        this.name = name;
        this.index = index;
        this.users = users;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public ArrayList<User> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<User> users) {
        this.users = users;
    }
}

