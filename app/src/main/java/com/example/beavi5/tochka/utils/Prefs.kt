package com.example.beavi5.tochka.utils

import android.content.Context
import android.content.SharedPreferences

class Prefs(context: Context) {
    private val PREFS_FILENAME = "com.example.beavi5.tochka.prefs"
    private val PHOTO_URL = "photoUrl"
    private val USER_NAME = "userName"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, 0);

    var photoUrl: String
        get() = prefs.getString(PHOTO_URL, "")
        set(value) = prefs.edit().putString(PHOTO_URL, value).apply()
    var userName: String
        get() = prefs.getString(USER_NAME, "")
        set(value) = prefs.edit().putString(USER_NAME, value).apply()
}