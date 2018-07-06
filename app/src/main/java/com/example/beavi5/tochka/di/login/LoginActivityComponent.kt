package com.example.beavi5.tochka.di.login

import com.example.beavi5.tochka.di.ActivityScope
import com.example.beavi5.tochka.ui.login.LoginActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(LoginActivityModule::class))
interface LoginActivityComponent {
    fun inject(loginActivity: LoginActivity)
}