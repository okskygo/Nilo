package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.databinding.ObservableInt;
import android.databinding.ViewDataBinding;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;

import com.android.annotations.NonNull;
import com.silver.cat.nilo.R;
import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.view.KeyPreImeEditText;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class MainViewModel implements ViewModel {

    public final ObservableInt logoVisibility;
    public final ObservableInt backgroundColor;
    public final ObservableInt editVisibility;
    public final ObservableInt textVisibility;
    public final ObservableInt recyclerVisibility;
    private final Disposable disposable;
    private final View rootView;
    private final Subject<Boolean> searchSubject = PublishSubject.create();
    private Context context;
    private boolean searchExpand;
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
    private boolean scrollDisable;
    public final View.OnTouchListener scrollOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            return scrollDisable;
        }
    };

    public MainViewModel(@NonNull Context context, @NonNull ViewDataBinding dataBinding) {
        this.context = context;
        this.rootView = dataBinding.getRoot();
        this.logoVisibility = new ObservableInt(View.VISIBLE);
        this.editVisibility = new ObservableInt(View.GONE);
        this.textVisibility = new ObservableInt(View.VISIBLE);
        this.recyclerVisibility = new ObservableInt(View.VISIBLE);
        this.backgroundColor = new ObservableInt(ContextCompat.getColor(context, R.color
                .defaultBackground));
        this.disposable = searchSubject.debounce(500, TimeUnit.MICROSECONDS).observeOn
                (AndroidSchedulers.mainThread()).doOnNext
                (this::changeSearchStatus).subscribe();
    }

    public void onSearchClick(View view) {
        searchExpand = !searchExpand;
        searchSubject.onNext(searchExpand);
    }

    private void changeSearchStatus(boolean willSearchExpand) {
        View searchLayout = rootView.findViewById(R.id.search_layout);
        View recycler = rootView.findViewById(R.id.recycler_layout);
        View editText = rootView.findViewById(R.id.edit);
        RelativeLayout.LayoutParams searchLayoutLayoutParams = (RelativeLayout.LayoutParams)
                searchLayout.getLayoutParams();
        RelativeLayout.LayoutParams recyclerLayoutParams = (RelativeLayout.LayoutParams)
                recycler.getLayoutParams();
        if (willSearchExpand) {

            searchLayoutLayoutParams.addRule(RelativeLayout.BELOW, 0);
            searchLayoutLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP);

        } else {

            searchLayoutLayoutParams.addRule(RelativeLayout.BELOW, R.id.logo);
            searchLayoutLayoutParams.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0);

        }

        editText.requestFocus();
        logoVisibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
        editVisibility.set(willSearchExpand ? View.VISIBLE : View.GONE);
        textVisibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
        recyclerVisibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
        backgroundColor.set(willSearchExpand ? ContextCompat.getColor(context, R.color
                .dialogShadow) : ContextCompat.getColor(context, R.color
                .defaultBackground));
        scrollDisable = willSearchExpand;
        searchLayout.setLayoutParams(searchLayoutLayoutParams);
        recycler.setLayoutParams(recyclerLayoutParams);

        if (willSearchExpand) {
            Observable.just(1).delay(500, TimeUnit.MILLISECONDS).subscribe(o -> {
                InputMethodManager inputMethodManager = (InputMethodManager) context
                        .getSystemService
                                (Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
            });
        } else {
            InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService
                    (Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(editText.getWindowToken(), 0);

        }

    }


    @Override
    public void destroy() {
        disposable.dispose();
        context = null;
    }

}
