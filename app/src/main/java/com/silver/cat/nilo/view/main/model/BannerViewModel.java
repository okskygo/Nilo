package com.silver.cat.nilo.view.main.model;

import android.databinding.ObservableInt;
import android.view.View;

import com.silver.cat.nilo.widget.model.ViewModel;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class BannerViewModel implements ViewModel {

    public final ObservableInt visibility;
    private String index;

    public BannerViewModel(String index) {
        this.index = index;
        this.visibility = new ObservableInt(View.VISIBLE);
    }

    public void toggleSearchStatus(boolean willSearchExpand) {
        visibility.set(willSearchExpand ? View.GONE : View.VISIBLE);
    }

    public String getIndex() {
        return index;
    }

    @Override
    public void destroy() {

    }
}
