package com.example.beavi5.tochka.ui.main

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter (val view: IMainView, val repository: GitHubRepo) : IMainPresenter {
    private var searchQuery: String = ""
    private var currentPage: Int = 1
    private var subscription: Disposable? = null

    override fun onSearchQuery(searchText: String) {
        searchQuery = searchText
        currentPage = 1
        view.clearRVAdapter()
        onLoadMore()
    }

    override fun onLoadMore() {
        currentPage++
        subscription?.dispose()
        subscription = repository.loadMore(currentPage, this.searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onMoreLoaded(it.map {
                        UserPM(it.avatarUrl ?: "", it.login ?: "", it.score ?: 0.0, it.url
                                ?: "https://github.com/")
                    })
                },
                        { view.onError(it) })
    }

    override fun onSignOut() {
        view.startLoginActivity()
    }

    override fun onDestroy() {
        subscription?.dispose()
    }

}
