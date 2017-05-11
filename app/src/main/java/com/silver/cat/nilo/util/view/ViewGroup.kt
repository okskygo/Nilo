package com.liquable.curry.util.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * Created by liq on 2017/3/23.
 */

internal fun ViewGroup.inflate(layoutRes: Int): View = LayoutInflater.from(context).inflate(layoutRes, this, false)