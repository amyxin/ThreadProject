public class Main {
    public static void main(String[] args) {
        RunnableThread1 a=new RunnableThread1();
        new Thread(a).start();
        new Thread(a).start();
        new Thread(a).start();
    }
}
