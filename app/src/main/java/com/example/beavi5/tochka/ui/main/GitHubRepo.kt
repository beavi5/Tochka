package com.example.beavi5.tochka.ui.main

import com.example.beavi5.tochka.data.User
import com.example.beavi5.tochka.networks.GitHubService
import io.reactivex.Observable
import javax.inject.Inject

class GitHubRepo @Inject constructor(var gitHubService: GitHubService) {

    fun loadMore(page: Int, searchQuery: String): Observable<List<User>> {
        return gitHubService.listRepos("$searchQuery in:login", page).map { it.items }

    }


}