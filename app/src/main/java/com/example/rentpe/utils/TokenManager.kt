package com.example.rentpe.utils

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import com.example.rentpe.utils.Constants.ACCESS_TOKEN
import com.example.rentpe.utils.Constants.REFRESH_TOKEN
import com.example.rentpe.utils.Constants.TOKEN_FILE
import com.example.rentpe.activities.auth.LoginActivity
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class TokenManager @Inject constructor(@ApplicationContext context: Context) {

    var prefs = context.getSharedPreferences(TOKEN_FILE, Context.MODE_PRIVATE)
    var activity = context
    fun saveToken(access_token: String, refresh_token: String) {
        val editor = prefs.edit()
        editor.putString(ACCESS_TOKEN, access_token)
        editor.putString(REFRESH_TOKEN, refresh_token)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return prefs.getString(ACCESS_TOKEN, null)
    }

    fun logout() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
        val intent = Intent(activity.applicationContext, LoginActivity::class.java)
        intent.flags = FLAG_ACTIVITY_CLEAR_TASK or FLAG_ACTIVITY_NEW_TASK
        activity.startActivity(intent)
    }
}