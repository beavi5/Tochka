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

class LoginPresenter(val view: ILoginView): ILoginPresenter, GoogleApiClient.OnConnectionFailedListener {
    override fun onConnectionFailed(p0: ConnectionResult) {

    }

    lateinit var googleApiClient: GoogleApiClient
    val loginActivity = view as Activity
    val prefs = Prefs(loginActivity)

    init {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(loginActivity)
                .enableAutoManage(loginActivity as FragmentActivity, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()
    }

    fun onGoogleSignIn(){

    }

    fun onVkSignIn(){
        VKSdk.login(loginActivity, VKScopes.PHOTOS)
    }


    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?){


        if (!VKSdk.onActivityResult(requestCode, resultCode, data, object : VKCallback<VKAccessToken> {
                    override fun onResult(res: VKAccessToken?) {
                        // Пользователь успешно авторизовался
                        val photoRequest = VKApi.users().get(VKParameters.from(VKApiConst.FIELDS, "photo_max"))

                        photoRequest.executeWithListener(object : VKRequest.VKRequestListener() {
                            override fun onComplete(response: VKResponse) {
                                super.onComplete(response)

                                val user = (response.parsedModel as VKList<VKApiUserFull>)[0]
                                val photoUrl = user.photo_max//.getString("photo_max")
                                prefs.photoUrl = photoUrl
                                prefs.userName = "${user.first_name} ${user.last_name}"
                                startMainActivity()

                            }
                        })

                    }

                    override fun onError(error: VKError?) {
                        // Произошла ошибка авторизации (например, пользователь запретил авторизацию)
                    }
                }))

            if (requestCode == RC_SIGN_IN) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(data)
                val acct = result.signInAccount
                acct?.photoUrl?.let {
                    prefs.photoUrl = it.toString()
                }

                startMainActivity()

            }

    }
}