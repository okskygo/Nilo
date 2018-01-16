package com.silver.cat.nilo.util.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

internal fun ViewGroup.inflate(layoutRes: Int): View
        = LayoutInflater.from(context).inflate(layoutRes, this, false)