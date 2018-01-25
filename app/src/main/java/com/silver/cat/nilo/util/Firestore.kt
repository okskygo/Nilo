package com.silver.cat.nilo.util

import com.google.android.gms.tasks.Task
import io.reactivex.Completable


fun Task<Void>.completable(): Completable {
  return Completable.create { emitter ->
    this.addOnSuccessListener {
      emitter.onComplete()
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }
}