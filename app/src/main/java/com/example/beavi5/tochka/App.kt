package com.example.beavi5.tochka

import android.app.Application
import com.vk.sdk.VKSdk

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
    }


}