package com.silver.cat.nilo.view.main.model;

import android.databinding.ObservableBoolean;

import com.silver.cat.nilo.widget.model.ViewModel;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class LogoViewModel implements ViewModel, Observer {


    public final ObservableBoolean itemShow;


    public LogoViewModel() {
        this.itemShow = new ObservableBoolean(true);

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
        itemShow.set(!willSearchExpand);
    }
}
