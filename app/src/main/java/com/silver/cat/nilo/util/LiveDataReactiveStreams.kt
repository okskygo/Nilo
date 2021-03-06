package com.silver.cat.nilo.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.LiveDataReactiveStreams
import org.reactivestreams.Publisher


fun <T> Publisher<T>.toLiveData() = LiveDataReactiveStreams.fromPublisher(this) as LiveData<T>
