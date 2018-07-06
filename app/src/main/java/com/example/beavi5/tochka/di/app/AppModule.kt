package com.example.beavi5.tochka.di.app

import android.app.Application
import com.example.beavi5.tochka.utils.Prefs
import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module(includes = [GitHubModule::class])
class AppModule(val application: Application) {

    @Provides
    @Singleton
    fun application() = application

    @Provides
    @Singleton
    fun getContext() = application.applicationContext

    @Provides
    @Singleton
    fun getPreft() = Prefs(application)
}