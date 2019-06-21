package com.uber.rib.core.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class LayoutInflaterObservable extends Observable<View> {

    @NonNull
    private final Context context;
    @LayoutRes
    private final int layoutResId;
    @Nullable
    private final ViewGroup parent;

    public LayoutInflaterObservable(@NonNull Context context, int layoutResId, @Nullable ViewGroup parent) {
        this.context = context;
        this.layoutResId = layoutResId;
        this.parent = parent;
    }

    @Override
    protected void subscribeActual(Observer<? super View> observer) {
        LayoutInflaterDisposable disposable = new LayoutInflaterDisposable(observer, this);
        observer.onSubscribe(disposable);
        disposable.start();
    }

    private static class LayoutInflaterDisposable implements Disposable {

        private volatile boolean isDisposed = false;
        private Observer<? super View> observer;
        private LayoutInflaterObservable observable;

        LayoutInflaterDisposable(Observer<? super View> observer, LayoutInflaterObservable observable) {
            this.observer = observer;
            this.observable = observable;
        }

        @Override
        public void dispose() {
            synchronized (this) {
                isDisposed = true;
                observer = null;
                observable = null;
            }
        }

        @Override
        public boolean isDisposed() {
            return isDisposed;
        }

        void start() {
            LayoutInflater inflater;
            int layoutResId;
            ViewGroup parent;

            synchronized (this) {
                if (isDisposed || observable == null) return;
                inflater = new BasicInflater(observable.context);
                layoutResId = observable.layoutResId;
                parent = observable.parent;
            }

            try {
                View view = inflater.inflate(layoutResId, parent, false);

                if (!isDisposed && observer != null) {
                    observer.onNext(view);
                    observer.onComplete();
                }
            } catch (Throwable error) {
                if (!isDisposed && observer != null) {
                    observer.onError(error);
                }
            }

            observer = null;
            observable = null;
        }
    }

    private static class BasicInflater extends LayoutInflater {
        private static final String[] sClassPrefixList = {
                "android.widget.",
                "android.webkit.",
                "android.app."
        };

        BasicInflater(Context context) {
            super(context);
        }

        @Override
        public LayoutInflater cloneInContext(Context newContext) {
            return new BasicInflater(newContext);
        }

        @Override
        protected View onCreateView(String name, AttributeSet attrs) throws ClassNotFoundException {
            for (String prefix : sClassPrefixList) {
                View view = createView(name, prefix, attrs);

                if (view != null) {
                    return view;
                }
            }

            return super.onCreateView(name, attrs);
        }
    }
}
