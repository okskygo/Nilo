package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.databinding.ObservableInt;
import android.view.KeyEvent;
import android.view.View;

import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.view.KeyPreImeEditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class SearchViewModel implements ViewModel {

    public final ObservableInt editVisibility;
    public final ObservableInt textVisibility;
    private final Subject<Boolean> searchSubject = PublishSubject.create();
    private final Disposable disposable;
    private boolean searchExpand;
    private Context context;
    private OnSearchStatusChangeListener listener;
    public final KeyPreImeEditText.OnKeyPreImgListener onKeyPreImgListener = new KeyPreImeEditText
            .OnKeyPreImgListener() {

        @Override
        public boolean onKeyPreIme(int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                searchExpand = false;
                searchSubject.onNext(searchExpand);
            }
            return true;
        }
    };


    public SearchViewModel(Context context, OnSearchStatusChangeListener listener) {
        this.context = context;
        this.listener = listener;
        this.editVisibility = new ObservableInt(View.GONE);
        this.textVisibility = new ObservableInt(View.VISIBLE);
        this.disposable = searchSubject.debounce(500, TimeUnit.MICROSECONDS).observeOn
                (AndroidSchedulers.mainThread()).doOnNext
                (willSearchExpand -> {
                    listener.change(willSearchExpand);
                    toggleSearchStatus(willSearchExpand);
                }).subscribe();
    }

    public void onSearchClick(View view) {
        searchExpand = !searchExpand;
        searchSubject.onNext(searchExpand);
    }

    public void toggleSearchStatus(boolean willSearchExpand) {
        editVisibility.set(willSearchExpand ? View.VISIBLE : View.GONE);
        textVisibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
//        if (willSearchExpand) {
//            Observable.just(1).delay(500, TimeUnit.MILLISECONDS).subscribe(o -> {
//                InputMethodManager inputMethodManager = (InputMethodManager) context
//                        .getSystemService
//                                (Context.INPUT_METHOD_SERVICE);
//                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            });
//        } else {
//            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
//                    (Context.INPUT_METHOD_SERVICE);
//            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);
//
//        }

    }

    @Override
    public void destroy() {
        disposable.dispose();
        context = null;
        listener = null;
    }

}
