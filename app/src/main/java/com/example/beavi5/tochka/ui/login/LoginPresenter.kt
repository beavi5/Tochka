package com.example.beavi5.tochka.ui.login

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.support.v4.app.FragmentActivity
import android.util.Log
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

class LoginPresenter (val view: ILoginView, val context: Context, val prefs: Prefs) : ILoginPresenter, GoogleApiClient.OnConnectionFailedListener {
    private var googleApiClient: GoogleApiClient
    private val loginActivity = view as Activity
    private val fragmentActivity = view as FragmentActivity
    private val RC_SIGN_IN: Int = 322

    init {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(context)
                .enableAutoManage(fragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }


    override fun onGoogleSignIn() {
        if (!checkInternetConnection()) return
        signOutAll()
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        fragmentActivity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onVkSignIn() {
        if (!checkInternetConnection()) return
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
                        view.onError(Exception("Не удалось подключиться к VK"))
                    }
                }))

            if (requestCode == RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                if (result.isSuccess) {
                    view.startMainActivity()
                    val acct = result.signInAccount
                    acct?.photoUrl?.let {
                        prefs.photoUrl = it.toString()
                    }
                    acct?.displayName?.let {
                        prefs.userName = it
                    }

                }
            }

    }

    private fun signOutAll() {
        prefs.photoUrl = ""
        prefs.userName = ""
        try {
            Auth.GoogleSignInApi.signOut(googleApiClient)
            VKSdk.logout()
        } catch (e: Exception) {
            Log.d("signOut", "SignOut exception on method signOutAll")
        }
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
        view.onError(Exception("Ошибка подключения к Google"))
    }

    private fun checkInternetConnection(): Boolean {
        val cm: ConnectivityManager? = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val isConnected = cm?.activeNetworkInfo?.isConnected ?: false
        if (!isConnected) {
            view.onError(Error("Нет подключения к интернету!"))
        }
        return isConnected
    }
}
