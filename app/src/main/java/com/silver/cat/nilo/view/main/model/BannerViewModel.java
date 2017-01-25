package com.silver.cat.nilo.view.main.model;

import android.databinding.ObservableBoolean;
import android.databinding.ObservableInt;
import android.view.View;

import com.silver.cat.nilo.widget.model.ViewModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class BannerViewModel implements ViewModel, Observer {

    public final ObservableInt visibility;
    public final ObservableBoolean contentShow;
    private String index;

    public BannerViewModel(String index) {
        this.index = index;
        this.visibility = new ObservableInt(View.VISIBLE);
        this.contentShow = new ObservableBoolean(true);
    }

    public String getIndex() {
        return index;
    }

    @Override
    public void destroy() {

    }

    @Override
    public void update(Observable o, Object arg) {
        if (!(arg instanceof Boolean)) {
            return;
        }
        boolean willSearchExpand = ((Boolean) arg);
        contentShow.set(!willSearchExpand);
        visibility.set(willSearchExpand ? View.INVISIBLE : View.VISIBLE);
    }
}
