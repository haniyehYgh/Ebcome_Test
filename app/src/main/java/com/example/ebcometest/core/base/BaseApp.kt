package com.example.ebcometest.core.base

import androidx.multidex.BuildConfig
import androidx.multidex.MultiDexApplication
import timber.log.Timber


abstract class BaseApp : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}