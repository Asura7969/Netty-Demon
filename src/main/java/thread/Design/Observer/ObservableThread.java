package thread.Design.Observer;

public class ObservableThread<T> extends Thread implements Observable{

    private final TaskLifecycle<T> lifecycle;
    private final Task<T> task;
    private Cycle cycle;

    public ObservableThread(Task<T> task){
        this(new TaskLifecycle.EmptyLifecycle<>(),task);
    }

    public ObservableThread(TaskLifecycle<T> lifecycle,Task<T> task){
        super();
        if(task == null){
            throw new IllegalArgumentException("the task is required.");
        }
        this.lifecycle = lifecycle;
        this.task = task;
    }

    @Override
    public final void run() {
        this.update(Cycle.START,null,null);
        try{
            this.update(Cycle.RUNNING,null,null);
            T call = this.task.call();
            this.update(Cycle.DONE,call,null);
        }catch (Exception e){
            this.update(Cycle.ERROR,null,e);
        }
    }

    private void update(Cycle cycle,T result,Exception e){
        this.cycle = cycle;
        if(lifecycle == null){
            return;
        }
        try {
            switch (cycle){
                case START:
                    this.lifecycle.onStart(currentThread());
                    break;
                case RUNNING:
                    this.lifecycle.onRunning(currentThread());
                    break;
                case DONE:
                    this.lifecycle.onFinish(currentThread(),result);
                    break;
                case ERROR:
                    this.lifecycle.Error(currentThread(),e);
                    break;
            }
        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public Cycle getCycle() {
        return this.cycle;
    }
}
