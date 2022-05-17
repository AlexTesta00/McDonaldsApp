package com.example.mcdonalds.utils

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import com.example.mcdonalds.R

class MessageManager {

    companion object{

        fun displayNoGpsEnableMessage(activity : Activity){
            AlertDialog.Builder(activity)
                .setTitle("GPS Disattivato")
                .setMessage("Il gps risulta disabilitato, vuoi attivarlo?")
                .setPositiveButton(R.string.confirm){ _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
                }
                .setNegativeButton(R.string.not_confirm){_,_ ->
                    Toast.makeText(activity, "La mappa è stata disattivata :(", Toast.LENGTH_SHORT).show()
                }
                .show()
        }
    }
}