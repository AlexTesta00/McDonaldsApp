package com.example.mcdonalds.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.example.mcdonalds.MainActivity
import com.example.mcdonalds.R
import com.example.mcdonalds.utils.Constants

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Hide ActionBar
        supportActionBar?.hide()
        //Start MainActivity

        Handler(Looper.getMainLooper()).postDelayed({
            val openMainActivity = Intent(this@SplashScreen, MainActivity::class.java)
            startActivity(openMainActivity)}
            , Constants.SPLASH_SCREEN_DURATION)
    }
}