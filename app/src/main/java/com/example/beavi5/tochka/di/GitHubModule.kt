package com.example.beavi5.tochka.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.example.beavi5.tochka.networks.GitHubService
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory


@Module
class GitHubModule {

    @Provides
    fun retrofit() = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .baseUrl("https://api.github.com/")
            .build()

    @Provides
    fun getGitHubApi(retrofit: Retrofit) = retrofit.create(GitHubService::class.java)

}