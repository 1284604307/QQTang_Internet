package connect.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

public class HomeInfo {
    private String name;
    private String index;
    private ArrayList<User> users;

    public HomeInfo(String name, String index, Map<String,String> userMap) {
        this.name = name;
        this.index = index;

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

}
