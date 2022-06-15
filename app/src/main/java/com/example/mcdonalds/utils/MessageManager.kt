package com.example.mcdonalds.utils

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.provider.Settings
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.mcdonalds.R
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.model.QRGenerator

class MessageManager {

    companion object{

        fun displayNoGpsEnableMessage(activity : Activity){
            AlertDialog.Builder(activity)
                .setTitle("GPS Disattivato")
                .setMessage("Il gps risulta disabilitato, vuoi attivarlo?")
                .setPositiveButton(R.string.confirm){ _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
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

        fun displayNoPositionComputable(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Ops! Qualcosa è andato storto...")
                .setMessage("Qusta funzione al momento non è disponibile, riprova")
                .setPositiveButton("Ho capito"){ _, _ -> }
                .setCancelable(false)
                .show()
        }

        fun displayReplaceOrderMessage(activity: Activity, orderId : String, vararg itemName : String){
            AlertDialog.Builder(activity)
                .setTitle("Clona Ordine")
                .setMessage("Sei sicuro di voler replicare l'ordine?")
                .setPositiveButton(R.string.confirm){ _, _ ->
                    McOrder.cloneOrder(*itemName)
                    Toast.makeText(activity,
                                    "Tutti gli elementi sono stati aggiunti al carrello",
                                    Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton(R.string.not_confirm){_,_ -> }
                .setNeutralButton("Genera QR"){_,_ -> this.displayId(activity,orderId)}
                .setCancelable(false)
                .show()
        }

        fun displayNoHolderOrderPresent(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Ops! Qualcosa è andato storto...")
                .setMessage("L'ordine non è presente nei nostri database")
                .setPositiveButton("Ho capito"){ _, _ -> }
                .setCancelable(false)
                .show()
        }

        private fun displayId(activity: Activity,orderId: String){
            val qr = QRGenerator().generateQrFromString(orderId)
            val inflater = activity.layoutInflater
            val view = inflater.inflate(R.layout.alert_dialog_whit_image, null)
            val imageView = view.findViewById<ImageView>(R.id.img_alert_dialog)
            imageView.setImageBitmap(qr)
            AlertDialog.Builder(activity)
                .setTitle("Clona Ordine")
                .setMessage("Sei sicuro di voler replicare l'ordine?")
                .setPositiveButton(R.string.okay){ _, _ -> }
                .setCancelable(false)
                .setView(view)
                .show()
        }

        fun displayNoNetworkEnabled(activity: Activity){
            AlertDialog.Builder(activity)
                .setTitle("Il dispositivo non è connesso")
                .setMessage("Il dispositivo non è connesso ad internet, vuoi abilitare la connessione?")
                .setPositiveButton(R.string.confirm){ _, _ ->
                    activity.startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
                }
                .setNegativeButton(R.string.not_confirm){_,_ -> }
                .setCancelable(false)
                .show()
        }

    }
}