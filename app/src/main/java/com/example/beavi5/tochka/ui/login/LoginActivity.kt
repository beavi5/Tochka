package com.example.beavi5.tochka.ui.login

import android.content.Intent
import android.os.Bundle
import com.example.beavi5.tochka.App
import com.example.beavi5.tochka.BuildConfig
import com.example.beavi5.tochka.R
import com.example.beavi5.tochka.di.login.LoginActivityModule
import com.example.beavi5.tochka.ui.base.BaseActivity
import com.example.beavi5.tochka.ui.main.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import javax.inject.Inject


class LoginActivity : BaseActivity(), ILoginView {

    @Inject
    lateinit var presenter: ILoginPresenter

    override fun setupActivityComponent() {
        App.getAppComponent()
                .add(LoginActivityModule(this))
                .inject(this)
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvVersion.text = "Version ${BuildConfig.VERSION_NAME}"
        btnSignInGoogle.setOnClickListener {
            presenter.onGoogleSignIn()
        }
        btnSignInVK.setOnClickListener {
            presenter.onVkSignIn()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        presenter.onActivityResult(requestCode, resultCode, data)
    }

    override fun startMainActivity() {
        startActivity(Intent(this, MainActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP))
        finish()
    }

    override fun onError(e: Throwable) {
        showError(e)
    }

}
