package cn.iotzone.mindlocal.udp;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;

public final class RxBus {

    private static RxBus sInstance = new RxBus();
    private final PublishSubject<Object> mSubject = PublishSubject.create();

    public static RxBus get() {
        return sInstance;
    }

    private RxBus() {
    }

    public <T> Observable<T> toObservable(Class<T> eventType) {
        return mSubject.ofType(eventType);
    }

    public void send(Object event) {
        if (event != null) {
            mSubject.onNext(event);
        }
    }
}