public class RunnableThread1  implements  Runnable{
    private int z=0;
    public void run() {
        for(int i=0;i<1;i++){
            System.out.println(Thread.currentThread().getName());
            System.out.println(Thread.currentThread().isDaemon());
            System.out.println("当前线程组名字："+Thread.currentThread().getThreadGroup().getName());
            System.out.println(z++);
        }
    }
}
