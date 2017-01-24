package com.silver.cat.nilo.view.main.model;

import com.silver.cat.nilo.R;
import com.silver.cat.nilo.widget.recycler.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

class MainAdapter extends BaseAdapter {

    private final List<Object> models = new ArrayList<>();
    private final List<BannerViewModel> bannerViewModels;
    private final SearchViewModel searchViewModel;
    private final LogoViewModel logoViewModel;

    MainAdapter(SearchViewModel searchViewModel, LogoViewModel logoViewModel,
                List<BannerViewModel> bannerViewModels) {
        this.searchViewModel = searchViewModel;
        this.logoViewModel = logoViewModel;
        this.bannerViewModels = bannerViewModels;
        models.add(logoViewModel);
        models.add(searchViewModel);
        models.addAll(bannerViewModels);
    }

    @Override
    protected Object getObjForPosition(int position) {
        return models.get(position);
    }

    @Override
    protected int getLayoutIdForPosition(int position) {

        Object object = models.get(position);
        if (object instanceof LogoViewModel) {
            return R.layout.holder_main_logo;
        } else if (object instanceof SearchViewModel) {
            return R.layout.holder_main_search;
        } else {
            return R.layout.holder_main_banner;
        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

}
