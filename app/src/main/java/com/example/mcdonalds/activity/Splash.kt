package com.example.mcdonalds.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.example.mcdonalds.R
import com.example.mcdonalds.utils.Constants
import pl.droidsonroids.gif.GifImageView

class Splash : AppCompatActivity() {

    private lateinit var gifImageView: GifImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        this.bindComponents()

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed({
            startActivity(Intent(this@Splash, LoginActivity::class.java))
            finish()
        }, Constants.SPLASH_SCREEN_DURATION)
    }

    private fun bindComponents(){
        this.gifImageView = findViewById(R.id.splash)
    }

}