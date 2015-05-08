package com.xiaomi.mitv.shop.network;

import java.util.concurrent.*;

/**
 * Created by niuyi on 2015/5/8.
 */
public abstract class MyBaseRequest implements Runnable {

    private static final BlockingQueue<Runnable> sPoolWorkQueue = new LinkedBlockingQueue<Runnable>(100);

    private static final Executor THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(6, 128, 1, TimeUnit.SECONDS, sPoolWorkQueue, new MyAbortPolicy());

    protected MyObserver mObserver;

    public void send(){
        THREAD_POOL_EXECUTOR.execute(this);
    }

    public interface MyObserver{
        void onRequestCompleted(MyBaseRequest request, DKResponse response);
        void onAbort();
    }

    public void setObserver(MyObserver myRequestObserver) {
        this.mObserver = myRequestObserver;
    }

    public void abort() {
        if(mObserver != null){
            mObserver.onAbort();
        }
    }

    static class MyAbortPolicy implements RejectedExecutionHandler{

        @Override
        public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
            if(r instanceof MyBaseRequest){
                MyBaseRequest request = (MyBaseRequest)r;
                request.abort();
            }
        }
    }

}
