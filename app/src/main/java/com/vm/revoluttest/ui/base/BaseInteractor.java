package com.vm.revoluttest.ui.base;

import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;

/**
 * Base interactor class that works with observable subscriptions
 */
@SuppressWarnings("WeakerAccess")
public abstract class BaseInteractor {

    protected CompositeDisposable subscriptions = new CompositeDisposable();

    public void onStart() {
        subscriptions = new CompositeDisposable();
    }

    public void onStop() {
        disposeSubscriptions();
    }

    protected void disposeSubscriptions(){
        if (!subscriptions.isDisposed()) {
            subscriptions.dispose();
        }
    }

    protected <T> void subscribeAsync(Observable<T> observable, Consumer<T> subscriber, Consumer<Throwable> error) {
        subscriptions.add(
                observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber, error)
        );
    }

    protected <T> void subscribeAsync(Flowable<T> observable, Consumer<T> subscriber, Consumer<Throwable> error) {
        subscriptions.add(
                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread(), false, 1)
                        .subscribe(subscriber, error)
        );
    }
}
