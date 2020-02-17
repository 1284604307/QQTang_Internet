import java.util.Timer;
import java.util.TimerTask;

public class test {
    public static void main(String[] args) {

        System.out.println("hahah");
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                System.out.println("3秒后");
                timer.cancel();
            }
        },3000);

    }
}
