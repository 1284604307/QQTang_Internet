package test;

public final class MsgUtils {
 
    private MsgUtils() {
    }
 
    public static String joinMsg(String socketId, String body) {
        return socketId+"*"+body+"\n";
    }
}