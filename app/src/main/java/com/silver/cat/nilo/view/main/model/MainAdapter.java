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
    private boolean searchExpand;

    MainAdapter(SearchViewModel searchViewModel, LogoViewModel logoViewModel) {
        models.add(logoViewModel);
        models.add(searchViewModel);
        this.searchViewModel = searchViewModel;
        this.logoViewModel = logoViewModel;
        bannerViewModels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            bannerViewModels.add(new BannerViewModel(String.valueOf(i)));
        }
        models.addAll(bannerViewModels);
    }

    @Override
    protected Object getObjForPosition(int position) {

        if (models.get(position) instanceof BannerViewModel) {
            ((BannerViewModel) models.get(position)).toggleSearchStatus(searchExpand);
            return models.get(position);
        }
        return models.get(position);

//        if (searchExpand) {
//            if (position == 0) {
//                return searchViewModel;
//            } else {
//                return models.get(position);
//            }
//        }
//
//        if (position == MainType.LOGO.ordinal()) {
//            return logoViewModel;
//        } else if (position == MainType.SEARCH.ordinal()) {
//            return searchViewModel;
//        } else {
//
//        }
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

//        if (searchExpand) {
//            if (position == 0) {
//                return R.layout.holder_main_search;
//            } else {
//                return R.layout.holder_main_banner;
//            }
//        }
//
//        if (position == MainType.LOGO.ordinal()) {
//            return R.layout.holder_main_logo;
//        } else if (position == MainType.SEARCH.ordinal()) {
//            return R.layout.holder_main_search;
//        } else {
//            return R.layout.holder_main_banner;
//        }
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void toggleLogoStatus(boolean show) {
        if (show) {
            models.add(0, logoViewModel);
            notifyItemInserted(0);
        } else {
            models.remove(0);
            notifyItemRemoved(0);
        }
    }

    public void toggleBannerStatus(boolean show) {
        if (show) {
//            models.addAll(models.size(), bannerViewModels);
            notifyItemRangeChanged(2, bannerViewModels.size());
        } else {
//            models.removeAll(bannerViewModels);
            notifyItemRangeChanged(1, bannerViewModels.size());
        }
    }

    public void toggleSearchStatus(boolean willSearchStatus) {
        this.searchExpand = willSearchStatus;

//        if (willSearchStatus) {
//            notifyItemRemoved(0);
//            notifyItemRangeChanged(1, models.size());
//        } else {
//            notifyItemInserted(0);
//            notifyItemRangeChanged(1, models.size());
//        }
    }

    public int getBannerSize() {
        return models.size();
    }

    private enum MainType {
        LOGO, SEARCH, SHORTCUTS, BANNER
    }

}
