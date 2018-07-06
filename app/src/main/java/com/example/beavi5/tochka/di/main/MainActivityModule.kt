package com.example.beavi5.tochka.di.main

import com.example.beavi5.tochka.di.ActivityScope
import com.example.beavi5.tochka.ui.main.GitHubRepo
import com.example.beavi5.tochka.ui.main.IMainPresenter
import com.example.beavi5.tochka.ui.main.MainActivity
import com.example.beavi5.tochka.ui.main.MainPresenter
import dagger.Module
import dagger.Provides


@Module
class MainActivityModule(private val activity: MainActivity) {

    @Provides
    @ActivityScope
    fun presenter(repository: GitHubRepo): IMainPresenter {
        return MainPresenter(activity, repository)
    }


}