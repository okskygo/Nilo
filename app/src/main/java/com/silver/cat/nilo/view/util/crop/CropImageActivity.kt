package com.silver.cat.nilo.view.util.crop

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.silver.cat.nilo.util.Optional
import com.silver.cat.nilo.view.BaseActivity
import com.silver.cat.nilo.view.util.permission.PermissionManager
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import io.reactivex.Observable
import rx_activity_result2.RxActivityResult
import java.io.Serializable


class CropImageActivity : BaseActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    CropImage.activity()
        .setGuidelines(CropImageView.Guidelines.ON)
        .start(this)
  }

  override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
    super.onActivityResult(requestCode, resultCode, data)
    if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
      val result = CropImage.getActivityResult(data)
      val intent = Intent()
      if (resultCode == Activity.RESULT_OK) {
        intent.putExtra("cropImageResult", CropImageResult(result.uri.toString()))
      } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
        intent.putExtra("cropImageResult", CropImageResult(null, result.error))
      }
      setResult(Activity.RESULT_OK, intent)
      finish()
    }
  }

  companion object {

    fun start(activity: Activity): Observable<Optional<CropImageResult>> {

      return PermissionManager.requestPermissions(activity,
          Manifest.permission.READ_EXTERNAL_STORAGE,
          Manifest.permission.WRITE_EXTERNAL_STORAGE).flatMap { granted ->
        if (granted) {
          RxActivityResult
              .on(activity)
              .startIntent(Intent(activity, CropImageActivity::class.java))
              .map {
                val data = it.data()
                if (it.resultCode() == Activity.RESULT_OK && data != null) {
                  if (data.extras != null) {
                    Optional.of(data.extras.getSerializable("cropImageResult") as CropImageResult)
                  } else {
                    Optional.empty<CropImageResult>()
                  }
                } else {
                  Optional.empty<CropImageResult>()
                }
              }
        } else {
          Observable.just(Optional.empty<CropImageResult>())
        }
      }
    }

  }
}

data class CropImageResult(val uri: String?, val error: Exception? = null) : Serializable