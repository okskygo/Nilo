package com.silver.cat.nilo.util

import io.reactivex.*


fun <T> Flowable<T>.toResult(schedulerProvider: SchedulerProvider): Flowable<Result<T>> {
  return compose { item: Flowable<T> ->
    item
        .map { Result.success(it) }
        .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
        .observeOn(schedulerProvider.ui())
        .startWith(Result.inProgress())
  }
}

fun <T> Observable<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
  return compose { item ->
    item
        .map { Result.success(it) }
        .onErrorReturn { e -> Result.failure(e.message ?: "unknown", e) }
        .observeOn(schedulerProvider.ui())
        .startWith(Result.inProgress())
  }
}

fun <T> Single<T>.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
  return toObservable().toResult(schedulerProvider)
}

fun <T> Completable.toResult(schedulerProvider: SchedulerProvider): Observable<Result<T>> {
  return toObservable<T>().toResult(schedulerProvider)
}

interface SchedulerProvider {
  fun ui(): Scheduler

  fun computation(): Scheduler

  fun newThread(): Scheduler

  fun io(): Scheduler
}