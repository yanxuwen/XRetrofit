package com.yanxuwen.xretrofit.converter;

import com.xretrofit.CallAdapter.CallAdapter;
import com.xretrofit.HttpException;
import com.xretrofit.Response;
import com.xretrofit.call.Call;

import java.lang.ref.WeakReference;
import java.lang.reflect.Type;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.exceptions.CompositeException;
import io.reactivex.exceptions.Exceptions;
import io.reactivex.plugins.RxJavaPlugins;

/**
 * @author bsnl_yanxuwen
 * @date 2021/2/8 10:19
 * Description :
 * rxjava 适配器
 */
class Rxjava2CallAdapter<T> implements CallAdapter<Observable, T> {

    private Type responseType;

    protected Rxjava2CallAdapter(Type responseType) {
        this.responseType = responseType;
    }


    @Override
    public Type responseType() {
        return responseType;
    }


    @Override
    public Observable adapt(Call<T> call) {
        Observable observable = new CallExecuteObservable(call);
        return observable;
    }

    final class CallExecuteObservable<T> extends Observable<T> {
        private final Call<T> call;


        CallExecuteObservable(Call<T> call) {
            this.call = call;

        }

        @Override
        protected void subscribeActual(Observer<? super T> observer) {
            CallDisposable disposable = new CallDisposable(call);
            observer.onSubscribe(disposable);
            Response<T> response = null;
            try {
                response = call.execute();
                if (response.isSuccessful()) {
                    observer.onNext((T) response.body());
                } else {
                    Throwable t = new HttpException(response);
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            } catch (Throwable t) {
                Exceptions.throwIfFatal(t);
                if (!disposable.isDisposed()) {
                    try {
                        observer.onError(t);
                    } catch (Throwable inner) {
                        Exceptions.throwIfFatal(inner);
                        RxJavaPlugins.onError(new CompositeException(t, inner));
                    }
                }
            }
            observer.onComplete();
        }

    }


    private static final class CallDisposable implements Disposable {
        private volatile boolean disposed;
        private WeakReference<Call> weakCall;

        CallDisposable(Call<?> call) {
            weakCall = new WeakReference<>(call);

        }

        @Override
        public void dispose() {
            disposed = true;
            if (weakCall != null) {
                weakCall.get().cancel();
            }
        }

        @Override
        public boolean isDisposed() {
            return disposed;
        }
    }
}