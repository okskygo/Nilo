package com.silver.cat.nilo.view.util

import android.content.res.Resources

/**
 * Created by liq on 2017/7/6.
 */

val Int.toDp: Int
  get() = (this / Resources.getSystem().displayMetrics.density).toInt()

val Int.toPx: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

val Float.toDp: Float
  get() = (this / Resources.getSystem().displayMetrics.density)

val Float.toPx: Float
  get() = (this * Resources.getSystem().displayMetrics.density)