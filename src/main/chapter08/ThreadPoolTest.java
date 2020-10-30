import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) {
        final ThreadPool threadPool=new BasicThreadPool(2,6,4,1000);

        for(int i=0;i<20;i++){
            threadPool.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
