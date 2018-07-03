package com.example.beavi5.tochka.ui.login

import android.content.Intent

interface ILoginView {
    fun onError(e: Throwable)
    fun startMainActivity()
}
interface ILoginPresenter {
    fun onVkSignIn()
    fun onGoogleSignIn()
    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?)

}