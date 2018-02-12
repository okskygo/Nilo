package com.silver.cat.nilo.view.util

import android.app.Dialog
import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.app.DialogFragment
import com.silver.cat.nilo.R

class ProgressDialogFragment : DialogFragment() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    isCancelable = false
  }

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
    val dialog = ProgressDialog(activity, theme)
    dialog.isIndeterminate = true
    dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER)
    dialog.setMessage(getString(R.string.message_sending))
    return dialog
  }

}