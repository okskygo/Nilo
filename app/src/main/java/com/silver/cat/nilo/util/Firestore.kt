package com.silver.cat.nilo.util

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import io.reactivex.Completable
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

inline fun <reified T> Task<DocumentSnapshot>.single(): Single<Result<T>> {
  return Single.create { emitter ->
    this.addOnSuccessListener {
      emitter.onSuccess(Result.success(it.toObject(T::class.java)))
    }.addOnFailureListener {
      emitter.onError(it)
    }
  }
}

inline fun <reified T> Task<QuerySnapshot>.listSingle(): Single<Result<List<T>>> {
  return Single.create { emitter ->
    this.addOnFailureListener {
      emitter.onError(it)
    }.addOnCompleteListener {
      if (it.isSuccessful) {
        emitter.onSuccess(Result.success(it.result.map { it.toObject(T::class.java) }))
      } else {
        emitter.onSuccess(Result.success(emptyList()))
      }
    }
  }
}

