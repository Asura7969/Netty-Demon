package thread.Design.Observer;

import java.util.concurrent.TimeUnit;

public class StartObserver2 {
    public static void main(String[] args) {
        final TaskLifecycle<String> lifecycle = new TaskLifecycle.EmptyLifecycle<String>(){
            @Override
            public void onFinish(Thread thread, String result) {
                System.out.println("the result is" + result);
            }
        };

        Observable observable = new ObservableThread<>(lifecycle,()-> {
            try {
                TimeUnit.SECONDS.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("finish done");
            return "Hello Observer";
        });
        observable.start();
    }
}