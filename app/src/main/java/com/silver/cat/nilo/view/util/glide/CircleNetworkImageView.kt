package com.silver.cat.nilo.view.util.glide

import android.content.Context
import android.util.AttributeSet

/**
 * default resource id must be BitmapDrawable
 */
class CircleNetworkImageView @JvmOverloads constructor(context: Context,
                                                       attrs: AttributeSet? = null,
                                                       defStyle: Int = 0)
  : RoundedNetworkImageView(context, attrs, defStyle) {

  init {
    isCircular = true
  }

}
