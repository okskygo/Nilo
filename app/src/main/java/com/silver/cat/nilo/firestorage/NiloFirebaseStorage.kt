package com.silver.cat.nilo.firestorage

import android.net.Uri
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.silver.cat.nilo.util.Optional
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NiloFirebaseStorage @Inject constructor() {

  private fun avatarReference() = FirebaseStorage.getInstance().getReference(
      "avatar")

  fun uploadAvatar(uid: String, uri: Uri): Flowable<Optional<Uri>> {
    val genUid = UUID.randomUUID().toString()
    return avatarReference()
        .child("$uid/$genUid")
        .putFile(uri)
        .flowable()
  }

}

fun UploadTask.flowable(): Flowable<Optional<Uri>> {
  return Flowable.create({ emitter ->
    this.addOnSuccessListener {
      emitter.onNext(Optional.of(it.downloadUrl))
    }.addOnFailureListener {
          emitter.onError(it)
        }
  }, BackpressureStrategy.BUFFER)
}