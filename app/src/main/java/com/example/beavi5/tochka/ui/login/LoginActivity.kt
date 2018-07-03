package com.example.beavi5.tochka.ui.login

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.ui.BaseActivity
import com.example.beavi5.tochka.utils.Prefs
import com.example.beavi5.tochka.ui.main.MainActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.api.*
import com.vk.sdk.api.model.VKScopes
import kotlinx.android.synthetic.main.activity_login.*
import com.vk.sdk.api.model.VKApiUserFull
import com.vk.sdk.api.model.VKList


class LoginActivity : BaseActivity(), ILoginView, GoogleApiClient.OnConnectionFailedListener {
    override fun onError(e: Throwable) {
        showError(e)
    }

    private val RC_SIGN_IN: Int = 322
    lateinit var googleApiClient: GoogleApiClient

    private lateinit var prefs: Prefs

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val presenter = LoginPresenter(this, this, this)


        prefs = Prefs(this)

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        googleApiClient = GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build()


        btnSignInGoogle.setOnClickListener {
            //            googleSignIn()
            presenter.onGoogleSignIn()
        }
        btnSignInVK.setOnClickListener {
            //            vkSignIn()
            presenter.onVkSignIn()
        }
    }

    private fun vkSignIn() {
        VKSdk.login(this, VKScopes.PHOTOS)
    }


    private fun googleSignIn() {
        Auth.GoogleSignInApi.signOut(googleApiClient);
        val signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient)
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

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

    private fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }
}
