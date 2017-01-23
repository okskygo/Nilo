package com.silver.cat.nilo.widget.recycler;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class PredictiveLinearLayoutManager extends LinearLayoutManager {

    public PredictiveLinearLayoutManager(Context context) {
        super(context);
    }

    public PredictiveLinearLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    public PredictiveLinearLayoutManager(Context context, AttributeSet attrs, int defStyleAttr,
                                         int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    public boolean supportsPredictiveItemAnimations() {
        return true;
    }
}
