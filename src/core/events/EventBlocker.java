package core.events;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Hook returned by EventDispatcher.notifyEvent. Allows blocking until all event handling is finished.
 */
public class EventBlocker {
    private final List<Future<?>> handlers;

    public EventBlocker(List<Future<?>> handlers) {
        this.handlers = handlers;
    }

    public void block(Duration d) throws ExecutionException, InterruptedException, TimeoutException {
        for (Future<?> h : handlers) {
            h.get(d.toMillis(), TimeUnit.MILLISECONDS);
        }
    }

    public boolean hasFinished() {
        return handlers.stream().allMatch(x -> x.isCancelled() || x.isDone());
    }
}
