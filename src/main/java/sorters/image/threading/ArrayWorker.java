package sorters.image.threading;

import java.util.concurrent.Callable;

public abstract class ArrayWorker<R> implements Callable<R>
{
	protected int startAt;
	protected int endAt;

    public ArrayWorker(int startAt, int endAt) 
    {
        this.startAt = startAt;
        this.endAt = endAt;
    }
}