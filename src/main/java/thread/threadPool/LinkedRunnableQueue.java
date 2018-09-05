package thread.threadPool;

import java.util.LinkedList;

public class LinkedRunnableQueue implements RunnableQueue {

    private final int limit;
    private final DenyPolicy denyPolicy;
    private final LinkedList<Runnable> runnableList = new LinkedList<>();
    private final ThreadPool threadPool;

    public LinkedRunnableQueue(int limit, DenyPolicy denyPolicy, ThreadPool threadPool) {
        this.limit = limit;
        this.denyPolicy = denyPolicy;
        this.threadPool = threadPool;
    }

    @Override
    public void offer(Runnable runnable) {
        synchronized (runnableList){
            if(runnableList.size() >= limit){
                //无法容纳新的任务时执行拒绝策略
                denyPolicy.reject(runnable,threadPool);
            }else {
                //将任务加入队尾,并且唤醒阻塞中的线程
                runnableList.addLast(runnable);
                runnableList.notifyAll();
            }
        }
    }

    @Override
    public Runnable take() throws InterruptedException {
        synchronized (runnableList){
            while (runnableList.isEmpty()){
                try {
                    //如果队列中没有可执行的任务,则当前线程将会挂起,进入runnableList关联的monitor waitset中等待被唤醒(新加的任务)
                    runnableList.wait();
                } catch (InterruptedException e) {
                    //被中断时需要将该异常抛出
                    throw e;
                }
            }
            return runnableList.removeFirst();
        }
    }

    @Override
    public int size() {
        synchronized (runnableList){
            //返回当前任务队列中的任务数
            return runnableList.size();
        }
    }
}
