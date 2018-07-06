package com.example.beavi5.tochka.di.app

import com.example.beavi5.tochka.di.login.LoginActivityComponent
import com.example.beavi5.tochka.di.login.LoginActivityModule
import com.example.beavi5.tochka.di.main.MainActivityComponent
import com.example.beavi5.tochka.di.main.MainActivityModule
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {
    fun add(module: LoginActivityModule): LoginActivityComponent
    fun add(module: MainActivityModule): MainActivityComponent
}