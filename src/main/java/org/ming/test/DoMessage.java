package org.ming.test;

public class DoMessage {
    public int playerID = 0;
    public Behavior behavior ;
    public String value = "";

    public DoMessage(Behavior behavior,String all){
        this.behavior = behavior;
        value = all;
    }



    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("{");
        sb.append("\"playerID\":")
                .append(playerID);
        sb.append(",\"behavior\":\"")
                .append(behavior).append('\"');
        sb.append(",\"value\":\"")
                .append(value).append('\"');
        sb.append('}');
        return sb.toString();
    }
}
