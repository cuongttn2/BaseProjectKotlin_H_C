package com.example.baseprojectkotlin_h_c.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class ToastUtils(context: Context) {
    private var toast: Toast? = null
    fun showToast(message: String?) {
        toast!!.setText(message)
        toast!!.duration = Toast.LENGTH_SHORT
        toast!!.show()
    }
    companion object : SingletonHolder<ToastUtils, Context>(::ToastUtils)

    init {
        toast = Toast.makeText(context, "", Toast.LENGTH_SHORT)
        toast!!.setGravity(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, 0, 70)
    }
}