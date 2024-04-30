package com.example.baseprojectkotlin_h_c.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class SharedPreferenceUtils private constructor(context: Context) {
    companion object : SingletonHolder<SharedPreferenceUtils, Context>(::SharedPreferenceUtils)

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("My_SharePref", Context.MODE_PRIVATE)

    inline fun <reified T> getObjModel(): T? {
        val value = getStringValue("Key_Obj_${T::class.java.name}")
        return if (value != null && value.isNotEmpty()) {
            Gson().fromJson(value, T::class.java)
        } else {
            null
        }
    }

    inline fun <reified T> setObjModel(value: T?){
        if (value!=null){
            putStringValue("Key_Obj_${T::class.java.name}",Gson().toJson(value))
        }
    }

    fun putStringValue(key: String?, value: String?) {
        val editor = sharedPreferences.edit()
        editor.putString(key, value).apply()
    }

    fun getStringValue(key: String?): String? {
        return sharedPreferences.getString(key, "")
    }

    fun putBooleanValue(key: String, value: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(key, value).apply()
    }

    fun getBooleanValue(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getBooleanValueWithTrueDefault(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }

    fun putIntValue(key: String?, value: Int) {
        val editor = sharedPreferences.edit()
        editor.putInt(key, value).apply()
    }

    fun getIntValue(key: String?): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun putLongValue(key: String?, value: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(key, value).apply()
    }

    fun getLongValue(key: String?): Long {
        return sharedPreferences.getLong(key, 0L)
    }


}