package com.example.beavi5.tochka

import android.app.Application
import com.example.beavi5.tochka.di.app.AppComponent
import com.example.beavi5.tochka.di.app.AppModule
import com.example.beavi5.tochka.di.app.DaggerAppComponent
import com.vk.sdk.VKSdk

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        VKSdk.initialize(this)
        appComponent = DaggerAppComponent.builder().appModule(AppModule(this)).build()
    }

    companion object {
        private lateinit var appComponent: AppComponent
        fun getAppComponent() = appComponent
    }
}