package com.silver.cat.nilo.widget.recycler;

import android.databinding.BindingAdapter;
import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {

    @BindingAdapter("android:layout_height")
    public static void setLayoutHeight(View view, int height) {

        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
        int realHeight;
        if (height > 0) {
            realHeight = Math.round(height * view.getContext().getResources().getDisplayMetrics()
                    .density);
        } else {
            realHeight = height;
        }
        System.out.println("realHeight = " + realHeight);
        layoutParams.height = realHeight;
        view.setLayoutParams(layoutParams);
    }

    public BaseViewHolder onCreateViewHolder(ViewGroup parent,
                                             int viewType) {
        LayoutInflater layoutInflater =
                LayoutInflater.from(parent.getContext());
        ViewDataBinding binding = DataBindingUtil.inflate(
                layoutInflater, viewType, parent, false);
        return new BaseViewHolder(binding);
    }

    public void onBindViewHolder(BaseViewHolder holder, int position) {
        Object obj = getObjForPosition(position);
        holder.bind(obj);
    }

    @Override
    public int getItemViewType(int position) {
        return getLayoutIdForPosition(position);
    }

    protected abstract Object getObjForPosition(int position);

    protected abstract int getLayoutIdForPosition(int position);
}
