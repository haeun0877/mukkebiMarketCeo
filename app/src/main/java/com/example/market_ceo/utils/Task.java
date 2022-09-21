package com.example.market_ceo.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.ArrayDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class Task<T> {

    private static final Handler sHandler = new Handler(Looper.getMainLooper());

    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int CORE_POOL_SIZE = CPU_COUNT + 1;
    private static final int MAXIMUM_POOL_SIZE = CPU_COUNT * 2 + 1;
    private static final int KEEP_ALIVE = 1;
    private static final ThreadFactory sThreadFactory = new ThreadFactory() {
        private final AtomicInteger mCount = new AtomicInteger(1);
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, "Task #" + mCount.getAndIncrement());
            t.setDaemon(true);
            t.setPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);
            return t;
        }
    };
    private static final ArrayDeque<Runnable> sRejectedTasks = new ArrayDeque<Runnable>();
    private static final BlockingQueue<Runnable> sPoolWorkQueue = new PriorityBlockingQueue<Runnable>(100);
    private static final Executor sExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, sPoolWorkQueue, sThreadFactory);

    private final Callable<T> mCaller;
    private final ComparableFutureTask<T> mFutureTask;

    public Task() {
        mCaller = new Callable<T>() {
            @Override
            public T call() throws Exception {
                T result = onExecute();
                checkRejectedTasks();
                return result;
            }
        };
        mFutureTask = new ComparableFutureTask<T>(mCaller) {
            @Override
            protected void done() {
                try {
                    postResult(get());
                } catch(InterruptedException e) {
                    e.printStackTrace();
                } catch(ExecutionException e) {
                    e.printStackTrace();
                } catch (CancellationException e) {
                    postResult(null);
                }
            }
        };
    }

    protected abstract T onExecute();

    protected void onPostExecute(T result) {

    }

    private void postResult(final T result) {
        sHandler.post(new Runnable() {
            @Override
            public void run() {
                onPostExecute(result);
            }
        });
    }

    public Task<T> setPriority(int priority) {
        mFutureTask.priority = priority;
        return this;
    }

    public int getPriority() {
        return mFutureTask.priority;
    }

    public void execute() {
        try {
            sExecutor.execute(mFutureTask);
        } catch(RejectedExecutionException e) {
            addRjectedTask(mFutureTask);
        }
    }

    public boolean isDone() {
        return mFutureTask.isDone();
    }

    public boolean isCancelled() {
        return mFutureTask.isCancelled();
    }

    public void cancel(boolean mayInterruptIfRunning) {
        mFutureTask.cancel(mayInterruptIfRunning);
    }

    private static class ComparableFutureTask<T> extends FutureTask<T> implements Comparable<ComparableFutureTask<T>> {
        private volatile int priority = Integer.MAX_VALUE / 2;

        public ComparableFutureTask(Runnable runnable, T result) {
            super(runnable, result);
        }

        public ComparableFutureTask(Callable<T> callable) {
            super(callable);
        }

        @Override
        public int compareTo(ComparableFutureTask<T> o) {
            return Integer.valueOf(priority).compareTo(o.priority);
        }
    }

    private static void runOnUiThread(Runnable r) {
        if(Looper.myLooper() == Looper.getMainLooper()) {
            r.run();
        } else {
            sHandler.post(r);
        }
    }

    private static void addRjectedTask(final Runnable r) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(r != null && !sRejectedTasks.contains(r)) {
                    sRejectedTasks.offer(r);
                }
            }
        });
    }

    private static void checkRejectedTasks() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Runnable r = sRejectedTasks.peek();
                if(r != null) {
                    try {
                        sExecutor.execute(r);
                        sRejectedTasks.poll();
                    } catch(RejectedExecutionException e) {

                    }
                }
            }
        });
    }
}
