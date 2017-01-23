package com.silver.cat.nilo.view.main.model;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.silver.cat.nilo.widget.model.ViewModel;
import com.silver.cat.nilo.widget.recycler.PredictiveLinearLayoutManager;

/**
 * Created by xiezhenyu on 2017/1/23.
 */

public class RecyclerViewModel implements ViewModel, OnSearchStatusChangeListener {

    private final SearchViewModel searchViewModel;
    private final LogoViewModel logoViewModel;
    private final MainAdapter adapter;
    private final PredictiveLinearLayoutManager layoutManager;
    private Context context;
    private RecyclerView recyclerView;
    private int lastOffset;

    public RecyclerViewModel(Context context, RecyclerView recyclerView) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.searchViewModel = new SearchViewModel(context, this);
        this.logoViewModel = new LogoViewModel();
        this.adapter = new MainAdapter(searchViewModel, logoViewModel);

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
        int offset = 0;
        if (willSearchExpand) {
            int position = layoutManager.findFirstVisibleItemPosition();
            View view = recyclerView.getChildAt(position);
            if (view != null) {
                offset = view.getTop();
            }
        }

        adapter.toggleSearchStatus(willSearchExpand);
        adapter.toggleLogoStatus(!willSearchExpand);
        adapter.toggleBannerStatus(!willSearchExpand);
//        if (willSearchExpand) {
//            adapter.notifyItemRemoved(0);
//            adapter.notifyItemRangeRemoved(2, 30);
//        } else {
//            adapter.notifyItemInserted(0);
//            adapter.notifyItemRangeInserted(2, 30);
//        }

//        Single.just(offset).delay(recyclerView.getItemAnimator().getAddDuration(),
//                TimeUnit.MICROSECONDS).observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Consumer<Integer>() {
//
//                    @Override
//                    public void accept(Integer offset) throws Exception {
//
//                    }
//                });
        layoutManager.scrollToPositionWithOffset(0, lastOffset);
        lastOffset = offset;
//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapter.toggleBannerStatus(!willSearchExpand);
//            }
//        }, recyclerView.getItemAnimator().getAddDuration());
//
//        recyclerView.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                adapter.notifyItemRangeChanged(1, adapter.getBannerSize());
//            }
//        }, recyclerView.getItemAnimator().getAddDuration());


    }
}
