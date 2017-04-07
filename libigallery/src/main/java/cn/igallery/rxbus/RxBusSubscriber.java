package cn.igallery.rxbus;

import rx.Subscriber;

/**
 * Created by enid on 2017/1/5.
 */

public abstract class RxBusSubscriber<T> extends Subscriber<T>{

    @Override
    public void onCompleted() {

    }

    @Override
    public void onError(Throwable e) {

    }

    @Override
    public void onNext(T t) {
        onEvent(t);
    }

    protected abstract void onEvent(T t);
}
