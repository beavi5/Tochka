package com.example.beavi5.tochka.ui.base

import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

open class BaseActivity: AppCompatActivity() {

    fun showProgress(){
        progressBar.visibility = View.VISIBLE
    }

    fun hideProgress(){
        progressBar.visibility = View.GONE
    }

    fun showError(e: Throwable){
        Toast.makeText(applicationContext, e.message?: "Невозможно получить данные", Toast.LENGTH_LONG).show()
    }

}