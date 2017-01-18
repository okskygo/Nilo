package com.silver.cat.nilo.widget.recycler;

import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;

import com.silver.cat.nilo.BR;

/**
 * Created by xiezhenyu on 2017/1/18.
 */
class BaseViewHolder extends RecyclerView.ViewHolder {

    private final ViewDataBinding binding;

    public BaseViewHolder(ViewDataBinding binding) {
        super(binding.getRoot());
        this.binding = binding;
    }

    public void bind(Object obj) {
        if (obj == null) {
            return;
        }
        binding.setVariable(BR.model, obj);
        binding.executePendingBindings();
    }

}
