package com.silver.cat.nilo.view.util.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.support.design.widget.Snackbar
import android.support.v4.app.ActivityCompat
import android.view.View
import com.silver.cat.nilo.R
import io.reactivex.Observable
import java.io.Serializable

class PermissionManager {

  companion object {

    fun requestPermissions(activity: Activity,
                           vararg permissions: String): Observable<Boolean> {

      if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
        return Observable.just(true)
      }

      val temporaryDeny = permissions.filter {
        PackageManager.PERMISSION_DENIED == ActivityCompat.checkSelfPermission(activity, it)
      }
      if (temporaryDeny.isEmpty()) {
        return Observable.just(true)
      }

      return PermissionHolderActivity.start(activity, temporaryDeny.toTypedArray()).map {
        if (it.isPresent) {
          val resultPermission = it.get()
          if (resultPermission.hasPermanentDeny()) {
            gotoAppSetting(activity)
          } else if (resultPermission.hasTemporaryDeny()) {
            alertPermission(activity)
          }
          resultPermission.allGranted()
        } else {
          false
        }
      }
    }

    fun gotoAppSetting(activity: Activity) {
      val builder = AlertDialog.Builder(activity)

      builder.setTitle(R.string.permission_request)
          .setMessage(R.string.permission_all_rationale)
          .setPositiveButton(R.string.ok) { _, _ ->
            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.fromParts("package", activity.packageName, null))
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            activity.startActivity(intent)
          }
          .setCancelable(false)
          .show()
    }

    fun alertPermission(activity: Activity) {
      val rootLayout: View = activity.findViewById(android.R.id.content)
      Snackbar.make(rootLayout, R.string.permission_all_rationale, Snackbar.LENGTH_SHORT).show()
    }

  }

}

class ResultPermission : Serializable {
  private val allGranted: Boolean
  private val permanentDeny = ArrayList<String>()
  private val temporaryDeny = ArrayList<String>()

  constructor(allGranted: Boolean) {
    this.allGranted = allGranted
  }

  constructor(allGranted: Boolean,
              temporaryDeny: List<String>,
              permanentDeny: List<String>) {
    this.allGranted = allGranted
    this.temporaryDeny.addAll(temporaryDeny)
    this.permanentDeny.addAll(permanentDeny)
  }

  fun hasPermanentDeny(): Boolean {
    return permanentDeny.isNotEmpty()
  }

  fun hasTemporaryDeny(): Boolean {
    return temporaryDeny.isNotEmpty()
  }

  fun allGranted(): Boolean {
    return allGranted
  }

}

