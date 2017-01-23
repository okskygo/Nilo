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


    public final ObservableInt backgroundColor;
    private Context context;


    public MainViewModel(@NonNull Context context) {
        this.context = context;
        this.backgroundColor = new ObservableInt(ContextCompat.getColor(context, R.color
                .defaultBackground));

    }


    @Override
    public void destroy() {
        context = null;
    }

}
