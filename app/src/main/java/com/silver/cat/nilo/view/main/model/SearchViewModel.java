package com.silver.cat.nilo.view.main.model;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.silver.cat.nilo.view.search.SearchActivity;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class SearchViewModel {

    private Activity activity;

    public SearchViewModel(Activity activity) {
        this.activity = activity;
    }

    public void onClickSearch(View view) {
        activity.startActivity(new Intent(activity, SearchActivity.class));
    }
}
