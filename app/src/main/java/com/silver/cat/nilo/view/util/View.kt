package com.silver.cat.nilo.view.util

import android.view.View
import com.jakewharton.rxbinding2.view.clicks
import io.reactivex.Observable
import java.util.concurrent.TimeUnit

/**
 * Created by Rex on 2018/1/16.
 */

fun View.clickThrottleFirst(): Observable<Unit> {
    return this.clicks().throttleFirst(1, TimeUnit.SECONDS)
}