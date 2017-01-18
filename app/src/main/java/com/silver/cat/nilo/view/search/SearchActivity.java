package com.silver.cat.nilo.view.search;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.silver.cat.nilo.R;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

/**
 * Created by xiezhenyu on 2017/1/18.
 */

public class SearchActivity extends RxAppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search);
    }
}
