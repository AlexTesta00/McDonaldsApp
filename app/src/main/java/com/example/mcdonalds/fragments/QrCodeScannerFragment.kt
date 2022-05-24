package com.example.mcdonalds.fragments

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.util.SparseArray
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.isNotEmpty
import com.example.mcdonalds.R
import com.example.mcdonalds.utils.MessageManager
import com.example.mcdonalds.utils.Permission
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import java.lang.Exception


class QrCodeScannerFragment : Fragment() {

    private lateinit var cameraView : SurfaceView
    private lateinit var cameraSource : CameraSource
    private lateinit var detector: BarcodeDetector

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(activity != null){
            this.bindComponents(activity as AppCompatActivity)

            //Check Camera Permission
            if(!Permission.isCameraEmabled(activity as AppCompatActivity)){
                this.requestCameraPermission(activity as AppCompatActivity)
            }else{
                this.setUpComponents(activity as AppCompatActivity)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_qr_code_scanner, container, false)
    }


    private fun bindComponents(activity: AppCompatActivity){
        this.cameraView = activity.findViewById(R.id.camera_surface_view)
        this.cameraView.holder.addCallback(this.surgaceCallBack)
    }

    private fun requestCameraPermission(activity: AppCompatActivity){
        ActivityCompat.requestPermissions(activity, arrayOf(Manifest.permission.CAMERA),
            Permission.CAMERA_PERMISSION
        )
    }

    private fun setUpComponents(activity: AppCompatActivity){
        this.detector = BarcodeDetector.Builder(activity).build()
        this.cameraSource = CameraSource.Builder(activity, detector).setAutoFocusEnabled(true).build()
        this.detector.setProcessor(this.processor)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Permission.CAMERA_PERMISSION && grantResults.isNotEmpty()){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                this.setUpComponents(activity as AppCompatActivity)
            }else{
                MessageManager.displayNoCameraEnabled(activity as AppCompatActivity)
            }
        }
    }

    private val surgaceCallBack = object : SurfaceHolder.Callback{
        override fun surfaceCreated(p0: SurfaceHolder) {
            setUpComponents(activity as AppCompatActivity)
        }

        override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {
            cameraSource.stop()
        }

        override fun surfaceDestroyed(surfaceHolder: SurfaceHolder) {
            try {
                if (ActivityCompat.checkSelfPermission(
                        activity as AppCompatActivity,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    return
                }
                cameraSource.start(surfaceHolder)
            }catch (exception : Exception){
                Log.d("error", "Qualcosa non è andato a bun fine $exception")
            }
        }

    }

    private val processor = object : Detector.Processor<Barcode>{
        override fun release() {
            TODO("Not yet implemented")
        }

        override fun receiveDetections(detections: Detector.Detections<Barcode>) {
            if(detections.detectedItems.isNotEmpty()){
                val qrCodes : SparseArray<Barcode> = detections.detectedItems
                val code = qrCodes.valueAt(0)
                //TODO do somenthing when detect object
                Toast.makeText(activity, "Detected $code", Toast.LENGTH_SHORT).show()
            }else{
                //TODO not scanning text
            }
        }

    }
}