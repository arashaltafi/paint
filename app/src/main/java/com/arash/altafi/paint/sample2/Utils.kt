package com.arash.altafi.paint.sample2

import android.view.View
import com.google.android.material.snackbar.Snackbar

const val STORAGE_PERMISSION_CODE = 1
const val GALLERY = 2

fun snackBarMsg(view: View, message: String) {
    val snackBar = Snackbar.make(view, message, Snackbar.LENGTH_LONG)
    snackBar.show()
}