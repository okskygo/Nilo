package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.databinding.ObservableInt;
import android.support.transition.TransitionManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.android.annotations.NonNull;
import com.silver.cat.nilo.R;
import com.silver.cat.nilo.view.main.MainAdapter;
import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.recycler.PredictiveLinearLayoutManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class MainViewModel extends java.util.Observable implements ViewModel {

    public final ObservableInt backgroundColor;
    public final MainAdapter adapter;
    public final PredictiveLinearLayoutManager layoutManager;
    private final SearchViewModel searchViewModel;
    private final LogoViewModel logoViewModel;
    private final List<BannerViewModel> bannerViewModels;
    private Context context;
    private int lastOffset;


    public MainViewModel(@NonNull Context context, SearchViewModel.OnSearchStatusChangeListener
            listener) {
        this.context = context;
        this.backgroundColor = new ObservableInt(ContextCompat.getColor(context, R.color
                .defaultBackground));

        this.searchViewModel = new SearchViewModel(context, listener);
        this.logoViewModel = new LogoViewModel();
        this.bannerViewModels = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            BannerViewModel bannerViewModel = new BannerViewModel(String.valueOf(i));
            addObserver(bannerViewModel);
            bannerViewModels.add(bannerViewModel);
        }
        addObserver(logoViewModel);
        addObserver(searchViewModel);

        this.layoutManager = new PredictiveLinearLayoutManager(context);
        this.adapter = new MainAdapter(searchViewModel, logoViewModel, bannerViewModels);

    }

    @BindingAdapter({"adapter", "layout_manager"})
    public static void setAdapter(RecyclerView recyclerView, MainAdapter adapter, RecyclerView
            .LayoutManager layoutManager) {

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void destroy() {
        context = null;
        logoViewModel.destroy();
        searchViewModel.destroy();
        for (BannerViewModel bannerViewModel : bannerViewModels) {
            bannerViewModel.destroy();
        }
    }

    public void change(RecyclerView recyclerView, boolean willSearchExpand) {
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
        notifyObservers(willSearchExpand);

        recyclerView.setBackgroundColor(ContextCompat.getColor(context, willSearchExpand ? R
                .color.dialogShadow : R.color.defaultBackground));
    }


}
