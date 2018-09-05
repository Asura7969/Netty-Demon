package thread.threadPool;

public class RunnableDenyException extends RuntimeException {
    public RunnableDenyException(String msg) {
        super(msg);
    }
}
