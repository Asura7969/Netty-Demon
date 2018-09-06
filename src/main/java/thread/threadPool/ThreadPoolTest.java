package thread.threadPool;

import java.util.concurrent.TimeUnit;

public class ThreadPoolTest {
    public static void main(String[] args) throws InterruptedException {
        final ThreadPool threadPool = new BaseThreadPool(2,6,4,1000);
        for (int i = 0; i < 20; i++) {
            threadPool.execute(()->{
                try {
                    TimeUnit.SECONDS.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        for (;;){
            //不断输出线程池的信息
//            System.out.println("getActiveCount:" + threadPool.getActiveCount());
//            System.out.println("getQueueSize:" + threadPool.getQueueSize());
//            System.out.println("getCoreSize:" + threadPool.getCoreSize());
//            System.out.println("getMaxSize:" + threadPool.getMaxSize());
//            System.out.println("======================================");
//            TimeUnit.SECONDS.sleep(5);

            TimeUnit.SECONDS.sleep(12);
            threadPool.shutdown();
            Thread.currentThread().join();
        }
    }
}
