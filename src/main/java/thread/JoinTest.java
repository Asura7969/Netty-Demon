package thread;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class JoinTest {
    public static void main(String[] args) throws InterruptedException {
        //定义两个线程
        List<Thread> threads = IntStream.range(1,3).mapToObj(JoinTest::create).collect(Collectors.toList());

        threads.forEach(Thread::start);

        //执行两个线程的join方法后，main线程处于blocked状态，join方法是被主线程调用的
        for (Thread thread : threads) {
            thread.join();
        }

        //线程循环输出
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + "#" + i);
            shortSleep();
        }
    }


    private static Thread create(int seq){
        return new Thread(()->{
            for (int i = 0; i < 10; i++) {
                System.out.println(Thread.currentThread().getName() + "#" + i);
                shortSleep();
            }
        },String.valueOf(seq));
    }

    private static void shortSleep(){
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
