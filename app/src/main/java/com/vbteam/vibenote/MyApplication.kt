package com.vbteam.vibenote

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyApplication : Application() {
//    @SuppressLint("StaticFieldLeak")
//    companion object {
//        private lateinit var context: Context
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//        context = applicationContext
//    }
}