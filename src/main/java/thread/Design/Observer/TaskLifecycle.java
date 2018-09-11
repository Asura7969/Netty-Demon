package thread.Design.Observer;

public interface TaskLifecycle<T> {
    //任务启动时会触发onStart方法
    void onStart(Thread thread);

    //任务正在运行时会触发onRunning方法
    void onRunning(Thread thread);

    //任务运行结束时，会触发onFish方法,其中result是任务执行结束后的结果
    void onFinish(Thread thread,T result);

    //任务执行报错时会触发onError方法
    void Error(Thread thread,Exception e);

    class EmptyLifecycle<T> implements TaskLifecycle<T>{

        @Override
        public void onStart(Thread thread) {

        }

        @Override
        public void onRunning(Thread thread) {

        }

        @Override
        public void onFinish(Thread thread, T result) {

        }

        @Override
        public void Error(Thread thread, Exception e) {

        }
    }
}
