package com.example.mcdonalds.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import com.example.mcdonalds.MainActivity
import com.example.mcdonalds.R
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.Permission

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Hide ActionBar
        supportActionBar?.hide()

        if(Permission.checkNetworkIsEnabled(this@SplashScreen)){
            //Start MainActivity
            Handler(Looper.getMainLooper()).postDelayed({
                val openMainActivity = Intent(this@SplashScreen, MainActivity::class.java)
                startActivity(openMainActivity)}
                , Constants.SPLASH_SCREEN_DURATION)
        }else{
            //If the phone is offline
            //Create Dialog for open the internet settings
            //Else close app
            AlertDialog.Builder(this@SplashScreen)
                .setTitle("La connessione dati non è attiva")
                .setMessage("Vuoi attivarla?")
                .setIcon(R.drawable.ic_not_signals)
                .setPositiveButton("Si"){_,_ -> startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))}
                .setNegativeButton("No") {_,_ -> System.exit(1)}
                .setCancelable(false)
                .show()
        }
    }
}