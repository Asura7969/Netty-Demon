package thread.threadPool;

import io.netty.util.concurrent.DefaultThreadFactory;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class BaseThreadPool extends Thread implements ThreadPool{

    private final int initSize;
    private final int maxSize;
    private final int coreSize;
    private int activeCount;

    private final ThreadFactory threadFactory;
    private final RunnableQueue runnableQueue;
    private volatile boolean isShutdown = false;
    //工作线程队列
    private final Queue<ThreadTask> threadQueue = new ArrayDeque<>();
    private final static DenyPolicy DEFAULT_DENY_POLICY = new DenyPolicy.DiscardDenyPolicy();
    private final static ThreadFactory DEFAULT_THREAD_FACTORY = new DefaultThreadFactory();
    private final long keepAliveTime;
    private final TimeUnit timeUnit;

    public BaseThreadPool(int initSize,int maxSize,int coreSize,int queueSize){
        this(initSize, maxSize, coreSize, DEFAULT_THREAD_FACTORY,queueSize,DEFAULT_DENY_POLICY,10,TimeUnit.SECONDS);
    }

    public BaseThreadPool(int initSize,int maxSize,int coreSize,
                          ThreadFactory threadFactory,int queueSize,
                          DenyPolicy denyPolicy,long keepAliveTime,TimeUnit timeUnit){
        this.initSize = initSize;
        this.maxSize = maxSize;
        this.coreSize = coreSize;
        this.threadFactory = threadFactory;
        this.runnableQueue = new LinkedRunnableQueue(queueSize,denyPolicy,this);
        this.keepAliveTime = keepAliveTime;
        this.timeUnit = timeUnit;
        this.init();
    }

    private void init(){
        start();
        for (int i = 0; i < initSize; i++) {
            newThread();
        }
    }

    private void newThread(){
        //创建任务线程,并且启动
        InternalTask internalTask = new InternalTask(runnableQueue);
        Thread thread = this.threadFactory.createThread(internalTask);
        ThreadTask threadTask = new ThreadTask(thread,internalTask);
        threadQueue.offer(threadTask);
        this.activeCount++;
        thread.start();
    }

    private void removeThread(){
        //从线程中移除某个线程
        ThreadTask threadTask = threadQueue.remove();
        threadTask.internalTask.stop();
        this.activeCount--;
    }


    /**
     * 提交任务
     * @param runnable
     */
    @Override
    public void execute(Runnable runnable) {
        if(this.isShutdown){
            throw new IllegalStateException("The thread pool is destory");
        }
        //提交任务只是简单地往任务队列中插入runnable
        this.runnableQueue.offer(runnable);
    }

    @Override
    public void run(){
        while (!isShutdown && !isInterrupted()){
            try{
                timeUnit.sleep(keepAliveTime);
            }catch (InterruptedException e){
                isShutdown = true;
                break;
            }
            synchronized (this){
                if(isShutdown){
                    break;
                }
                //当前队列中有任务尚未处理,并且activeCount < coreSize 则继续扩容
                if(runnableQueue.size() > 0 && activeCount < coreSize){
                    for (int i = initSize; i < coreSize; i++) {
                        newThread();
                    }
                    //主要目的不让线程直接扩容到maxSize
                    continue;
                }
                //当前队列中有任务尚未处理,并且activeCount < maxSize 则继续扩容
                if(runnableQueue.size() > 0 && activeCount < maxSize){
                    for (int i = coreSize; i < maxSize; i++) {
                        newThread();
                    }
                }

                if(runnableQueue.size() > 0 && activeCount > coreSize){
                    for (int i = coreSize; i < activeCount; i++) {
                        removeThread();
                    }
                }
            }
        }
    }
    //ThreadTask只是InternalTask和Thread的一个组合
    private static class ThreadTask{
        Thread thread;
        InternalTask internalTask;
        public ThreadTask(Thread thread,InternalTask internalTask){
            this.thread = thread;
            this.internalTask = internalTask;
        }
    }

    @Override
    public void shutdown() {
        synchronized (this){
            if(isShutdown){
                return;
            }
            isShutdown = true;
            threadQueue.forEach(threadTask -> {
                threadTask.internalTask.stop();
                threadTask.thread.interrupt();
            });
            this.interrupt();
        }
    }

    @Override
    public int getInitSize() {
        if (isShutdown) {
            throw new IllegalStateException("The Pool is destory");
        }
        return this.initSize;
    }

    @Override
    public int getMaxSize() {
        if (isShutdown) {
            throw new IllegalStateException("The Pool is destory");
        }
        return this.maxSize;
    }

    @Override
    public int getCoreSize() {
        if (isShutdown) {
            throw new IllegalStateException("The Pool is destory");
        }
        return this.coreSize;
    }

    @Override
    public int getQueueSize() {
        if (isShutdown) {
            throw new IllegalStateException("The Pool is destory");
        }
        return this.runnableQueue.size();
    }

    @Override
    public int getActiveCount() {
        synchronized (this){
            return this.activeCount;
        }
    }

    @Override
    public boolean isShutdown() {
        return this.isShutdown;
    }

    private static class DefaultThreadFactory implements ThreadFactory{
        private static final AtomicInteger GROUP_COUNTER = new AtomicInteger();
        private static final ThreadGroup group = new ThreadGroup("MyThreadPool-" + GROUP_COUNTER.getAndDecrement());
        private static final AtomicInteger COUNTER = new AtomicInteger(0);

        @Override
        public Thread createThread(Runnable runnable){
            return new Thread(group,runnable,"thread-pool-" + COUNTER.getAndDecrement());
        }
    }
}
