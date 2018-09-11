package thread.Design.Observer;

import java.util.concurrent.TimeUnit;

public class StartObserver {
    public static void main(String[] args) {
        Observable observable = new ObservableThread<>(()-> {
            try {
                TimeUnit.SECONDS.sleep(10);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            System.out.println("finish done");
            return null;
        });
        observable.start();
    }
}
