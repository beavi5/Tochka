package com.example.beavi5.tochka.networks


import com.example.beavi5.tochka.data.UserList
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query


interface GitHubService {
        @GET("search/users")
        fun listRepos(@Query("q") searchQuery: String, @Query("page") page: Int): Observable<UserList>
}