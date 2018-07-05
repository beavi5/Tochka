package com.example.beavi5.tochka.di

import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import com.example.beavi5.tochka.networks.GitHubService
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
class GitHubModule {

    @Singleton
    @Provides
    fun retrofit(gcf: GsonConverterFactory, rxj2caf: RxJava2CallAdapterFactory) = Retrofit.Builder()
            .addConverterFactory(gcf)
            .addCallAdapterFactory(rxj2caf)
            .baseUrl("https://api.github.com/")
            .build()

    @Singleton
    @Provides
    fun getGitHubApi(retrofit: Retrofit) = retrofit.create(GitHubService::class.java)

    @Singleton
    @Provides
    fun provideGsonConverterFactory() = GsonConverterFactory.create()

    @Singleton
    @Provides
    fun provideRxJava2CallAdapterFactory() = RxJava2CallAdapterFactory.create()
}