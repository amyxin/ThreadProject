import com.sun.deploy.appcontext.AppContext;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BasicThreadPool extends Thread implements ThreadPool {

    private  final int initSize;

    private  final int maxSize;

    private final int coreSize;

    private int activeCount;

    private final ThreadFactory threadFactory;

    private final RunnableQueue runnableQueue;

    private volatile  boolean isShutDown=true;

    private  final  static DenyPolicy DEFAULT_DENY_POLICY=new DenyPolicy.DiscardDenyPolicy();

    private  final  static ThreadFactory DEFAULT_THREAD_FACTORY=new DefaultThreadFactory();

    private final Queue<ThreadTask> threadQueue=new ArrayDeque<>();

    private final long keepAliveTime;

    private final TimeUnit timeUnit;

    public BasicThreadPool( int initSize, int maxSize, int coreSize, ThreadFactory threadFactory, int queueSize,DenyPolicy denyPolicy,long keepAliveTime, TimeUnit timeUnit) {
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize,denyPolicy,this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    public BasicThreadPool( int initSize, int maxSize, int coreSize, int queueSize) {
        this(initSize,maxSize,coreSize,DEFAULT_THREAD_FACTORY,queueSize,DEFAULT_DENY_POLICY,10,TimeUnit.SECONDS);
    }

    private void init() {
        start();
        for (int i = 0; i <initSize ; i++) {
            newThread();
        }
    }


    @Override
    public void execute(Runnable runnable) {
        if(this.isShutDown){
            throw new IllegalStateException(" the thread pool is destory ");
        }
        this.runnableQueue.offer(runnable);
    }

    private void newThread(){
        InternalTask internalTask=new InternalTask(runnableQueue);
        Thread thread=this.threadFactory.createThread(internalTask);
        ThreadTask threadTask=new ThreadTask(thread,internalTask);
        threadQueue.offer(threadTask);

        this.activeCount++;
        thread.start();
    }

    @Override
    public void shutdown() {
        synchronized (this){
            if(isShutDown) return;
            isShutDown=true;
            threadQueue.forEach(threadTask -> {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });
            this.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        return 0;
    }

    @Override
    public int getMaxSize() {
        return 0;
    }

    @Override
    public int getCoreSize() {
        return 0;
    }

    @Override
    public int getQueueSize() {
        return 0;
    }

    @Override
    public int getActiveCount() {
        return 0;
    }

    @Override
    public boolean isShutdown() {
        return false;
    }

    private static class ThreadTask{
        public ThreadTask(Thread thread,InternalTask internalTask) {
            this.thread=thread;
            this.internalTask=internalTask;
        }
        Thread thread;
        InternalTask internalTask;
    }

    @Override
    public void run() {
        while (!isShutDown&&!isInterrupted()){
            try{
                timeUnit.sleep(keepAliveTime);
            }
            catch(InterruptedException e){
                isShutDown=true;
                break;
            }
            synchronized (this){
                if(isShutDown)
                    break;
                if(runnableQueue.size()>0&&activeCount<coreSize){
                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    continue;
                }
                if(runnableQueue.size()>0&&activeCount<maxSize){
                    for (int i = coreSize; i <maxSize ; i++) {
                        newThread();
                    }
                }
                if(runnableQueue.size()==0&&activeCount>coreSize){
                    for (int i = coreSize; i < activeCount; i++) {
                        removeThread();
                    }
                }
            }
        }
    }

    private void removeThread() {
        ThreadTask threadTask=threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }

    private static class DefaultThreadFactory implements ThreadFactory {
        private static  final AtomicInteger GROUP_COUNTER=new AtomicInteger(1);

        private static final ThreadGroup group=new ThreadGroup("MyThreadPool-"+GROUP_COUNTER.getAndDecrement());

        private static final AtomicInteger COUNTER=new AtomicInteger(0);



        @Override
        public Thread createThread(Runnable runnable) {
            return new Thread(group,runnable,"thread-pool"+COUNTER.getAndDecrement());
        }
    }
}
