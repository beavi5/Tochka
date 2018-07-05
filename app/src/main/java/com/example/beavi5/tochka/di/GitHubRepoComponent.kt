package com.example.beavi5.tochka.di

import com.example.beavi5.tochka.ui.main.GitHubRepo
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = arrayOf(GitHubModule::class))
interface GitHubRepoComponent {
    fun injectGitHubRepo(gitHubRepo: GitHubRepo)
}