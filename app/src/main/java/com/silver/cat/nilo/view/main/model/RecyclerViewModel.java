package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.silver.cat.nilo.R;
import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.recycler.PredictiveLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class RecyclerViewModel extends Observable implements ViewModel,
        SearchViewModel.OnSearchStatusChangeListener {

    private final SearchViewModel searchViewModel;
    private final LogoViewModel logoViewModel;
    private final MainAdapter adapter;
    private final PredictiveLinearLayoutManager layoutManager;
    private final List<BannerViewModel> bannerViewModels;
    private Context context;
    private RecyclerView recyclerView;
    private int lastOffset;
    private boolean searchExpand;

    public RecyclerViewModel(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.searchViewModel = new SearchViewModel(context, this);
        this.logoViewModel = new LogoViewModel();
        this.bannerViewModels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BannerViewModel bannerViewModel = new BannerViewModel(String.valueOf(i));
            addObserver(bannerViewModel);
            bannerViewModels.add(bannerViewModel);
        }
        this.adapter = new MainAdapter(searchViewModel, logoViewModel, bannerViewModels);
        addObserver(logoViewModel);
        addObserver(searchViewModel);
        layoutManager = new PredictiveLinearLayoutManager(context);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public final void destroy() {
        context = null;
    }

    @Override
    public final void change(boolean willSearchExpand) {
        searchExpand = willSearchExpand;
        int offset = 0;
        if (willSearchExpand) {
            int position = layoutManager.findFirstVisibleItemPosition();
            View view = recyclerView.getChildAt(position);
            if (view != null) {
                offset = view.getTop();
            }
        }

        TransitionManager.beginDelayedTransition(recyclerView);

        layoutManager.scrollToPositionWithOffset(0, lastOffset);
        lastOffset = offset;

        setChanged();
        notifyObservers(searchExpand);

        recyclerView.setBackgroundColor(ContextCompat.getColor(context, willSearchExpand ? R
                .color.dialogShadow : R.color.defaultBackground));
    }
}
