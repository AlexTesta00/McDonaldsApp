package com.example.mcdonalds.fragments

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.mcdonalds.R
import com.example.mcdonalds.model.Category
import com.example.mcdonalds.model.DownloadManager
import com.example.mcdonalds.utils.Constants
import com.example.mcdonalds.utils.FragmentUtils
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.journeyapps.barcodescanner.ScanContract
import com.journeyapps.barcodescanner.ScanIntentResult
import com.journeyapps.barcodescanner.ScanOptions


class HomeFragment : Fragment() {

    private lateinit var qrButton : FloatingActionButton
    private lateinit var productView: RecyclerView
    private lateinit var categoryView : RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){

            //Bind All Components in View
            this.bindComponents(activity as AppCompatActivity)

            //Set all view listener
            this.setAllListener()

            //Change the AppBar Name
            FragmentUtils.changeAppBarName(activity as AppCompatActivity, getString(R.string.home))

            Log.v("vista", "OnViewCreated()")
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    private fun bindComponents(activity: Activity){
        this.qrButton = activity.findViewById(R.id.fab_qr)
        categoryView = activity.findViewById(R.id.rv_categories)
        productView = activity.findViewById(R.id.rv_products)
    }


    private fun setAllListener(){
        this.qrButton.setOnClickListener {
            val scanOptions = ScanOptions()
            scanOptions.setDesiredBarcodeFormats(ScanOptions.QR_CODE)
            scanOptions.setPrompt("Scannerizza un McDonald's QR Code")
            scanOptions.setCameraId(0)
            scanOptions.setBeepEnabled(false)
            scanOptions.setBarcodeImageEnabled(true)
            scanOptions.setOrientationLocked(false)
            barcodeLauncher.launch(scanOptions)
        }
    }

    // Register the launcher and result handler
    private val barcodeLauncher = registerForActivityResult(
        ScanContract()
    ) { result: ScanIntentResult ->
        if (result.contents == null) {
            Toast.makeText(activity, "Nessun codice QR valido rilevato", Toast.LENGTH_LONG).show()
        } else {
            downloadManager.recoverHistory(result.contents, activity as AppCompatActivity)
        }
    }

    //This is use to updateView when user change category
    companion object{
        private lateinit var downloadManager: DownloadManager
        var currentCategory : Category = Category(Constants.DEFAULT_CATEGORY)

        fun refreshProductView(category: Category){
            currentCategory = category
            downloadManager.changeCurrentCategory(category)
        }
    }

    override fun onResume() {
        super.onResume()

        if(activity != null){
            Log.v("vista", "OnResume() : Setto i recycler")
            //Init the download manager
            downloadManager = DownloadManager(this.productView,
                this.categoryView,
                Category(currentCategory.name),
                this.activity as AppCompatActivity)
        }else{
            Log.v("vista", "OnResume() : Attivita nulla")
        }
    }
}