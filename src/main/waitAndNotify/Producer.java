import com.sun.org.apache.xerces.internal.parsers.CachingParserPool;

import java.util.ArrayList;
import java.util.List;

public class Producer {

    public List<String>  list=new ArrayList<String>();

    public Producer(){
    }

    public void take() {
        synchronized(list){
            try{
                while(list.size()>=10){
                    System.out.println("wait后调用1111。。。。");
                    list.wait();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("wait后调用。。。。");
            list.add("1");
            list.notifyAll();
        }
    }

    public void produce() {
        synchronized (list){
            try{
                while(list.size()<=0){
                    list.wait();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
            list.remove(list.size()-1);
            list.notify();
        }
    }

}
