package com.silver.cat.nilo.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single


fun Task<Void>.completable(): Completable {
  return Completable.create { emitter ->
    this.addOnSuccessListener {
      emitter.onComplete()
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }
}

inline fun <reified T> Task<QuerySnapshot>.listSingle(): Single<List<T>> {
  return Single.create { emitter ->
    this.addOnFailureListener {
      emitter.onError(it)
    }.addOnCompleteListener { snap ->
      if (snap.isSuccessful) {
        emitter.onSuccess(snap.result.map { it.toObject(T::class.java) })
      } else {
        emitter.onSuccess(emptyList())
      }
    }
  }
}

inline fun <reified T> Task<DocumentSnapshot>.flowable(): Flowable<T> {
  return Flowable.create({ emitter ->
    this.addOnSuccessListener {
      emitter.onNext(it.toObject(T::class.java))
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }, BackpressureStrategy.BUFFER)
}

fun Task<Void>.finishFlowable(): Flowable<Boolean> {
  return Flowable.create({ emitter ->
    this.addOnSuccessListener {
      emitter.onNext(true)
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }, BackpressureStrategy.BUFFER)
}

inline fun <reified T> Task<QuerySnapshot>.listFlowable(): Flowable<List<T>> {
  return Flowable.create({ emitter ->
    this.addOnFailureListener {
      emitter.onError(it)
    }.addOnCompleteListener { snap ->
      if (snap.isSuccessful) {
        emitter.onNext(snap.result.map { it.toObject(T::class.java) })
      } else {
        emitter.onNext(emptyList())
      }
    }
  }, BackpressureStrategy.BUFFER)
}

