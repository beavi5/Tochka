package com.example.beavi5.tochka.di.main

import com.example.beavi5.tochka.di.ActivityScope
import com.example.beavi5.tochka.ui.main.MainActivity
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = arrayOf(MainActivityModule::class))
interface MainActivityComponent {
    fun inject(mainActivity: MainActivity)
}