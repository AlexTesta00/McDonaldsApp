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
import kotlin.system.exitProcess

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        //Hide ActionBar
        supportActionBar?.hide()

        //Check Internet Status
        if(Permission.checkNetworkIsEnabled(this@SplashScreen)){
            //Start MainActivity
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(Intent(this@SplashScreen, MainActivity::class.java))
                                                        }
                , Constants.SPLASH_SCREEN_DURATION)
        }else{
            //If the phone is offline
            //Create Dialog for open the internet settings
            //Else close app
            AlertDialog.Builder(this@SplashScreen)
                .setTitle(getString(R.string.connection_error))
                .setMessage(getString(R.string.connection_error_message))
                .setIcon(R.drawable.ic_not_signals)
                .setPositiveButton(getString(R.string.confirm)){ _, _ -> startActivity(Intent(Settings.ACTION_WIRELESS_SETTINGS))}
                .setNegativeButton(getString(R.string.not_confirm)) { _, _ -> exitProcess(1) }
                .setCancelable(false)
                .show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        finish()
    }
}