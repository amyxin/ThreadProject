import java.util.concurrent.ThreadPoolExecutor;

public class ThreadTest implements Runnable {
    public void run() {
        try{
            throw new RuntimeException();
        }
        catch(Exception e){
            System.out.println("捕获到异常");
        }

    }

    public static void main(String[] args) {
        //ThreadTest task=new ThreadTest();
        //Thread thread=new Thread(task);
        //thread.start();
        int a=0;
/*
        for(int i=0;i<99;i++){
            a=a++;
        }
        System.out.println(a);*/
        a=a=a+1;
        int b=0;
        for(int i=0;i<99;i++){
            b = ++ b;
        }
        System.out.println(b);
    }
}
