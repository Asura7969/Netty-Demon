package thread;

import java.util.concurrent.TimeUnit;

public class InterruptedTest {

    public static void main(String[] args) {
        //1 判断当前线程是否中断
        System.out.println("Main thread is interrupted?" + Thread.interrupted());

        //2中断当前线程
        Thread.currentThread().interrupt();

        //3判断当前线程是否已经中断
        System.out.println("Main thread is interrupted?" + Thread.currentThread().isInterrupted());

        try {
            //4 当前线程执行可中断方法
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {

            //5 捕获中断信号
            System.out.println("I will be interrupted still.");
        }
    }
}
