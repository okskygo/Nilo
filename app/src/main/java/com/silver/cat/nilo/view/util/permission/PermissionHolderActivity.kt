package com.silver.cat.nilo.view.util.permission

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import com.silver.cat.nilo.util.Optional
import io.reactivex.Observable
import rx_activity_result2.RxActivityResult
import java.util.*

class PermissionHolderActivity : Activity() {

  companion object {
    val RequestIntentKey = "REQUEST_PERMISSION"

    val ResultIntentKey = "RESULT_PERMISSION"

    val PermissionRequestCode = 1123

    fun start(activity: Activity,
              permissions: Array<String>): Observable<Optional<ResultPermission>> {
      val intent = Intent(activity, PermissionHolderActivity::class.java)
      intent.putExtra(RequestIntentKey, permissions)

      return RxActivityResult
          .on(activity)
          .startIntent(intent)
          .map {
            val data = it.data()
            if (it.resultCode() == Activity.RESULT_OK && data != null) {
              if (data.extras != null) {
                Optional.of(data.extras.getSerializable(ResultIntentKey) as ResultPermission)
              } else {
                Optional.empty<ResultPermission>()
              }
            } else {
              Optional.empty<ResultPermission>()
            }
          }
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    val permissions = intent.getStringArrayExtra(RequestIntentKey);
    if (permissions.isEmpty()) {
      finishForResult(ResultPermission(true))
      return
    }
    if (hasPermissions(*permissions)) {
      finishForResult(ResultPermission(true))
      return
    } else {
      ActivityCompat.requestPermissions(this, permissions, PermissionRequestCode)
    }
  }

  private fun finishForResult(resultPermission: ResultPermission) {
    val intent = Intent()
    intent.putExtra(ResultIntentKey, resultPermission)
    setResult(RESULT_OK, intent)
    finish()
  }

  override fun onRequestPermissionsResult(requestCode: Int,
                                          permissions: Array<String>,
                                          grantResults: IntArray) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults)

    if (PermissionRequestCode == requestCode) {
      if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
        finishForResult(ResultPermission(true))
      } else {
        val permanentDeny = ArrayList<String>()
        val temporaryDeny = ArrayList<String>()

        for (i in grantResults.indices) {
          val perm = permissions[i]
          if (grantResults[i] == PackageManager.PERMISSION_DENIED
              && ActivityCompat.shouldShowRequestPermissionRationale(this, perm)) {
            temporaryDeny.add(perm)
          } else if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
            permanentDeny.add(perm)
          }
        }
        finishForResult(ResultPermission(false, temporaryDeny, permanentDeny))
      }
    }
  }

  private fun hasPermissions(vararg permissions: String): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      permissions
          .filter {
            ActivityCompat.checkSelfPermission(this,
                it) != PackageManager.PERMISSION_GRANTED
          }
          .forEach { return false }
    }
    return true
  }
}
