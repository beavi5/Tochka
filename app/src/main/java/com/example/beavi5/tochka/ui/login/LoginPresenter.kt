package com.example.beavi5.tochka.ui.login

import android.app.Activity
import android.content.Intent
import android.support.v4.app.FragmentActivity
import com.example.beavi5.tochka.utils.Prefs
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKList
import com.vk.sdk.api.model.VKScopes

class LoginPresenter(val view: ILoginView) : ILoginPresenter, GoogleApiClient.OnConnectionFailedListener {
    var googleApiClient: GoogleApiClient

    private val loginActivity = view as Activity
    private val fragmentActivity = view as FragmentActivity
    private val prefs = Prefs(loginActivity)
    private val RC_SIGN_IN: Int = 322

    init {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(loginActivity)
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }


    override fun onGoogleSignIn() {
        signOutAll()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        fragmentActivity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onVkSignIn() {
        signOutAll()
        VKSdk.login(loginActivity, VKScopes.PHOTOS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {


        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken?) {
                        val photoRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max"))

                        photoRequest.executeWithListener(object : VKRequest.VKRequestListener() {
                            override fun onComplete(response: VKResponse) {
                                super.onComplete(response)

                                val user = (response.parsedModel as VKList<VKApiUserFull>)[0]
                                val photoUrl = user.photo_max
                                prefs.photoUrl = photoUrl
                                prefs.userName = "${user.first_name} ${user.last_name}"
                                view.startMainActivity()

                            }
                        })

                    }

                    override fun onError(error: VKError?) {
                        view.onError(Exception("Ошибка подключения к VK"))
                    }
                }))

            if (requestCode == RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result.isSuccess) {
                    val acct = result.signInAccount
                    acct?.photoUrl?.let {
                        prefs.photoUrl = it.toString()
                    }
                    acct?.displayName?.let {
                        prefs.userName = it
                    }

                    view.startMainActivity()
                }
            }

    }

    private fun signOutAll() {
        prefs.photoUrl = ""
        prefs.userName = ""
        Auth.GoogleSignInApi.signOut(googleApiClient)
        VKSdk.logout()
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        view.onError(Exception("Ошибка подключения к Google"))
    }
}
