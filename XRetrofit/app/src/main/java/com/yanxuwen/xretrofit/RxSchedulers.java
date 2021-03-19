package com.yanxuwen.xretrofit;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;

import com.uber.autodispose.AutoDispose;
import com.uber.autodispose.ObservableSubscribeProxy;
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider;

import io.reactivex.Observable;
import io.reactivex.ObservableConverter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bsnl_yanxuwen
 * @date 2021/3/19 10:52
 * Description :
 */
public class RxSchedulers {
    public static <T> ObservableConverter<T, ObservableSubscribeProxy> applySchedulers(LifecycleOwner owner) {
        return new ObservableConverter<T, ObservableSubscribeProxy>() {
            @NonNull
            @Override
            public ObservableSubscribeProxy apply(@NonNull Observable<T> upstream) {
                return upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .as(AutoDispose.autoDisposable(
                                AndroidLifecycleScopeProvider.from(owner, Lifecycle.Event.ON_DESTROY)));
            }
        };
    }
}
