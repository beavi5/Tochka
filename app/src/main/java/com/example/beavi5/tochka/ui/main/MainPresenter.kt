package com.example.beavi5.tochka.ui.main

import android.content.Context
import android.os.Bundle
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.GoogleApiClient
import com.google.firebase.auth.FirebaseAuth
import com.vk.sdk.VKSdk
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

class MainPresenter(val view: IMainView) : IMainPresenter {


    var subscription: Disposable? = null
    val repository = GitHubRepo()

    override fun onLoadMore(page: Int, searchQuery: String) {
        subscription?.dispose()
        subscription = repository.loadMore(page, searchQuery)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    view.onMoreLoaded(it.map {
                        UserPM(it.avatarUrl ?: "", it.login ?: "")
                    })
                }
                        , {
                    it.printStackTrace()
                    view.onError(it)
                })
    }

    override fun onSignOut() {
        FirebaseAuth.getInstance().signOut()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val gac = GoogleApiClient.Builder(view as Context)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build()
        gac.connect()
        gac.isConnectionCallbacksRegistered(object : GoogleApiClient.ConnectionCallbacks{
            override fun onConnectionSuspended(p0: Int) {
                Auth.GoogleSignInApi.signOut(gac)
            }

            override fun onConnected(p0: Bundle?) {
                Auth.GoogleSignInApi.signOut(gac)
            }

        }
)




        VKSdk.logout()
    }

}