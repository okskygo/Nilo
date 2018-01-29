package com.silver.cat.nilo.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.BackpressureStrategy
import io.reactivex.Completable
import io.reactivex.Flowable


fun Task<Void>.completable(): Completable {
  return Completable.create { emitter ->
    this.addOnSuccessListener {
      emitter.onComplete()
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }
}

inline fun <reified T> Task<DocumentSnapshot>.toFlowable(): Flowable<T> {
  return Flowable.create({ emitter ->
    this.addOnSuccessListener {
      emitter.onNext(it.toObject(T::class.java))
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }, BackpressureStrategy.BUFFER)
}

inline fun <reified T> Task<QuerySnapshot>.toListFlowable(): Flowable<List<T>> {
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

inline fun CollectionReference.whereListEqual(key: String, value: List<Any>): Query {
  this.whereEqualTo(key, value.first())
  return this
}

