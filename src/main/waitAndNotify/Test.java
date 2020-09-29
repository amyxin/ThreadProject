public class Test {
    public static void main(String[] args) {
        final Producer producer=new Producer();
        new Thread(){
            @Override
            public void run() {
                int i=0;
                while(i<12){
                    producer.take();
                    i++;
                }
            }
        }.start();
    }
}
