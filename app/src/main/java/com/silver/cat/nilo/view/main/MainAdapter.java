package com.silver.cat.nilo.view.main;

import com.silver.cat.nilo.R;
import com.silver.cat.nilo.widget.recycler.SingleLayoutAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

class MainAdapter extends SingleLayoutAdapter {

    private List<String> models = new ArrayList<>();

    MainAdapter() {
        super(R.layout.holder_main);
        for (int i = 0; i < 30; i++) {
            models.add(String.valueOf(i));
        }
    }

    @Override
    protected Object getObjForPosition(int position) {
        return models.get(position);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
