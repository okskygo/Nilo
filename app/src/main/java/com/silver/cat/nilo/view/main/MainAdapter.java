package com.silver.cat.nilo.view.main;

import com.silver.cat.nilo.R;
import com.silver.cat.nilo.widget.recycler.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

class MainAdapter extends BaseAdapter {

    enum MainViewType{
        HEADER,SEARCH,CATEGORY,BANNER
    }

    private List<String> models = new ArrayList<>();

    MainAdapter(){
        for (int i = 0; i < 30; i++) {
            models.add(String.valueOf(i));
        }
    }

    @Override
    protected Object getObjForPosition(int position) {
        return models.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {
        if(position == MainViewType.SEARCH.ordinal()){
            return R.layout.holder_main_search;
        }
        return R.layout.holder_main;
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
