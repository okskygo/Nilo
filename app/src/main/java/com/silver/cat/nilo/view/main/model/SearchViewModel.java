package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.view.KeyPreImeEditText;

import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class SearchViewModel implements ViewModel, Observer {

    public final ObservableInt editVisibility;
    public final ObservableInt textVisibility;
    private final Subject<Boolean> searchSubject = PublishSubject.create();
    private final Disposable disposable;
    private final Context context;
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
        this.disposable = searchSubject
                .debounce(100, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(listener::change)
                .delay(200, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .doOnNext(this::toggleSoftKeyboard)
                .subscribe();
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
        disposable.dispose();
    }

    @Override
    public void update(Observable o, Object arg) {
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
