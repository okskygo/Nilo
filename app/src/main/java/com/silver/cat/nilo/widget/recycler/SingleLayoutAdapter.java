package com.silver.cat.nilo.widget.recycler;

/**
 * Created by xiezhenyu on 2017/1/22.
 */

public abstract class SingleLayoutAdapter extends BaseAdapter {

    private final int layoutId;

    public SingleLayoutAdapter(int layoutId) {
        this.layoutId = layoutId;
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        return layoutId;
    }

}
