package com.example.mcdonalds.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
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

        fun displayNoGpsPermissionEnabled(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Non hai accettato i permessi")
                .setMessage("Non accettando i permessi per la localizzazione, non possiamo garantire un servizio efficente")
                .setPositiveButton("Voglio visualizzare i permessi"){ _, _ ->

                    //Request Permission
                    ActivityCompat.requestPermissions(activity,
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION),
                        Permission.GPS_PERMISSION_CODE
                    )
                }
                .setNegativeButton("Ho capito"){_,_ ->
                    Toast.makeText(activity, "La mappa è stata disattivata :(", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        fun displayNoCameraEnabled(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Non hai accettato i permessi")
                .setMessage("Non accettando i permessi per la fotocamera, non possiamo garantire un servizio efficente")
                .setPositiveButton("Voglio visualizzare i permessi"){ _, _ ->

                    //Request Permission
                    ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
                        Permission.CAMERA_PERMISSION
                    )
                }
                .setNegativeButton("Ho capito"){_,_ ->
                    Toast.makeText(activity, "La camera è stata disattivata :(", Toast.LENGTH_SHORT).show()
                }
                .show()
        }

        fun displayNoIngredientModifiable(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Non puoi eliminare questo ingrediente")
                .setMessage("Questo ingrediente è fondamentale per la costruzione del panino, non puoi eliminarlo")
                .setPositiveButton("Ho capito"){ _, _ -> }
                .setCancelable(false)
                .show()
        }
    }
}