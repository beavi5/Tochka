package com.example.beavi5.tochka.ui.main

interface IMainView {
   fun onMoreLoaded(data: List<UserPM>)
    fun onError(e: Throwable)
    fun startLoginActivity()
    fun clearRVAdapter()
}
interface IMainPresenter {
    fun onLoadMore()
    fun onSignOut()
    fun onDestroy()
    fun onSearchQuery(searchText: String)
}