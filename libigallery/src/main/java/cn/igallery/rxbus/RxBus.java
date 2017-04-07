package cn.igallery.rxbus;

import rx.Observable;
import rx.Subscription;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by enid on 2017/1/5.
 */

public class RxBus {
    private static volatile RxBus mInstance;
    private final Subject bus;
    private CompositeSubscription compositeSubscription;

    private RxBus() {
        this.bus = new SerializedSubject<>(PublishSubject.create());
        compositeSubscription = new CompositeSubscription();
    }

    /**
     * 单例模式
     * @return RxBus
     */
    public static RxBus getInstance() {
        if (mInstance == null) {
            synchronized (RxBus.class) {
                if (mInstance == null) {
                    mInstance = new RxBus();
                }
            }
        }
        return mInstance;
    }

    /**
     * 发送消息
     * @param object eventObject
     */
    public void post(Object object) {
        bus.onNext(object);
    }

    /**
     * 接收消息
     * @param eventType
     * @param <T>
     * @return
     */
    public <T>Observable<T> toObservable(Class<T> eventType) {
        return bus.ofType(eventType);
    }

    /**
     * 添加订阅
     * @param subscription
     */
    public void add(Subscription subscription){
        compositeSubscription.add(subscription);
    }

    /**
     * 移除订阅
     * @param subscription
     */
    public void remove(Subscription subscription) {
        compositeSubscription.remove(subscription);
    }

    /**
     * 移除所有订阅
     */
    public void clear() {
        compositeSubscription.clear();
    }
}
