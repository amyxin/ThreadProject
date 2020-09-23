public class newThread {
    public static void main(String[] args) {
        new Thread(){
            @Override
            public void run() {
                for(int i=0;i<5;i++){
                    System.out.println(Thread.currentThread().getName());
                    System.out.println(i);
                }
            }
        }.start();
        testThread a=new testThread();
        System.out.println("测试分割线");
        a.start();
    }
}

class testThread extends Thread{
    @Override
    public void run() {
        for(int i=0;i<5;i++){
            System.out.println(Thread.currentThread().getName());
            System.out.println(i);
        }
    }
}