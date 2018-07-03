package com.example.beavi5.tochka.ui.main

interface IMainView {
   fun onMoreLoaded(data: List<UserPM>)
    fun onError(e: Throwable)
}
interface IMainPresenter {
    fun onLoadMore(page:Int, searchQuery: String)
    fun onSignOut()
}