package thread.threadPool;

@FunctionalInterface
public interface ThreadFactory {
    Thread createThread(Runnable runnable);
}
