package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.view.KeyPreImeEditText;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class SearchViewModel implements ViewModel, java.util.Observer {

    public final ObservableInt editVisibility;
    public final ObservableInt textVisibility;
    private final Context context;
    private Subject<Boolean> searchSubject = PublishSubject.create();
    private boolean searchExpand;
    public final KeyPreImeEditText.OnKeyPreImgListener onKeyPreImgListener = new KeyPreImeEditText
            .OnKeyPreImgListener() {

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                searchExpand = false;
                searchSubject.onNext(false);
            }
            return true;
        }
    };

    public SearchViewModel(Context context, OnSearchStatusChangeListener listener) {
        this.context = context;
        this.editVisibility = new ObservableInt(View.GONE);
        this.textVisibility = new ObservableInt(View.VISIBLE);
        searchSubject.toFlowable(BackpressureStrategy.DROP)
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(willSearchExpand -> {
                    Flowable<Boolean> observable = Flowable.just(willSearchExpand);
                    if (willSearchExpand) {
                        return observable
                                .doOnNext(listener::change)
                                .delay(200, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(this::toggleSoftKeyboard);
                    } else {
                        return observable
                                .doOnNext(this::toggleSoftKeyboard)
                                .delay(200, TimeUnit.MILLISECONDS)
                                .observeOn(AndroidSchedulers.mainThread())
                                .doOnNext(listener::change);
                    }
                })
                .subscribe(new Subscriber<Boolean>() {
                    Subscription subscription;
                    @Override
                    public void onSubscribe(Subscription subscription) {
                        this.subscription = subscription;
                        subscription.request(1);
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        subscription.request(1);
                    }

                    @Override
                    public void onError(Throwable t) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    public void onSearchClick(View view) {
        searchExpand = !searchExpand;
        searchSubject.onNext(searchExpand);
    }

    private void toggleSoftKeyboard(boolean show) {
        if (show) {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                    (Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                    (Context.INPUT_METHOD_SERVICE);
            inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    @Override
    public void destroy() {
        searchSubject = null;
    }

    @Override
    public void update(java.util.Observable o, Object arg) {
        if (!(arg instanceof Boolean)) {
            return;
        }
        boolean willSearchExpand = ((Boolean) arg);
        editVisibility.set(willSearchExpand ? View.VISIBLE : View.GONE);
        textVisibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
    }

    /**
     * Created by xiezhenyu on 2017/1/23.
     */
    public static interface OnSearchStatusChangeListener {

        void change(boolean willSearchExpand);

    }
}
