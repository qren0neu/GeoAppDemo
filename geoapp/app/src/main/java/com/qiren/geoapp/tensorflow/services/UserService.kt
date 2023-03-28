package com.qiren.geoapp.tensorflow.services

import android.content.Context
import com.google.gson.Gson
import com.qiren.geoapp.tensorflow.MyApp
import com.qiren.geoapp.tensorflow.beans.User

object UserService {

    private const val PREFS_NAME = "MyPrefs"
    private const val USER_KEY = "user"

    private val gson = Gson()
    private val prefs = MyApp.instance.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun storeUser(user: User) {
        val userJson = gson.toJson(user)
        prefs.edit().putString(USER_KEY, userJson).apply()
    }

    fun getUser(): User? {
        val userJson = prefs.getString(USER_KEY, null)
        return gson.fromJson(userJson, User::class.java)
    }

    fun checkLogin(): Boolean {
        val userJson = prefs.getString(USER_KEY, null)
        return userJson != null
    }

    fun logout() {
        prefs.edit().remove(USER_KEY).apply()
    }
}