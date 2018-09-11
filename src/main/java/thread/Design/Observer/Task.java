package thread.Design.Observer;

@FunctionalInterface
public interface Task<T> {
    T call();
}
