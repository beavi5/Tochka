package com.example.beavi5.tochka.di.login

import android.content.Context
import com.example.beavi5.tochka.di.ActivityScope
import com.example.beavi5.tochka.ui.login.ILoginPresenter
import com.example.beavi5.tochka.ui.login.LoginActivity
import com.example.beavi5.tochka.ui.login.LoginPresenter
import com.example.beavi5.tochka.utils.Prefs
import dagger.Module
import dagger.Provides


@Module
class LoginActivityModule(private val activity: LoginActivity) {

    @Provides
    @ActivityScope
    fun presenter(context: Context, prefs: Prefs): ILoginPresenter {
        return LoginPresenter(activity, context, prefs)
    }
}