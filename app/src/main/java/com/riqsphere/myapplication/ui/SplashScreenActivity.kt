package com.riqsphere.myapplication.ui

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.postDelayed
import com.riqsphere.myapplication.R

class SplashScreenActivity : AppCompatActivity() {

    private val splashTime= 3000
    private lateinit var handler: Handler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        handler = Handler()

        handler.postDelayed({
            goToMainActivity()
        }, splashTime.toLong())

    }

    private fun goToMainActivity(){
        val mainActivityIntent = Intent(this,MainActivity::class.java)
        startActivity(mainActivityIntent)
        finish()
    }

}