package com.vm.revoluttest.ui.base;

public interface BaseCallback<T> {
    void onNext(T t);

    void onError(Throwable t);
}
