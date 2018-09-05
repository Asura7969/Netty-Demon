package thread.threadPool;

@FunctionalInterface
public interface DenyPolicy {
    void reject(Runnable runnable,ThreadPool threadPool);

    //该策略直接丢弃任务
    class DiscardDenyPolicy implements DenyPolicy{

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            //do nothing
        }
    }

    //该策略会向提交者抛出异常
    class AbortDenyPolicy implements DenyPolicy{

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            throw new RunnableDenyException("The runnable" + runnable + "will be about");
        }
    }

    //该策略会使任务提交者所在的线程中执行任务
    class RunnableDenyPolicy implements DenyPolicy{

        @Override
        public void reject(Runnable runnable, ThreadPool threadPool) {
            if(!threadPool.isShutdown()){
                runnable.run();
            }
        }
    }
}
