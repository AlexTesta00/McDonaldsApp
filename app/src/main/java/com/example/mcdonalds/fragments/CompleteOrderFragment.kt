package com.example.mcdonalds.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.mcdonalds.R
import com.example.mcdonalds.activity.HomeActivity
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.model.QRGenerator

class CompleteOrderFragment : Fragment() {

    private lateinit var mcName : TextView
    private lateinit var mcQrCode : ImageView
    private lateinit var mcId : TextView
    private lateinit var returnToHome : Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(activity != null){
            this.bindComponent(activity as AppCompatActivity)
            this.setAllListener()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_complete_order, container, false)
    }

    private fun bindComponent(activity: AppCompatActivity){
        this.mcName = activity.findViewById(R.id.txt_mc_name)
        this.mcId = activity.findViewById(R.id.txt_order_code)
        this.mcQrCode = activity.findViewById(R.id.img_qr_code)
        this.returnToHome = activity.findViewById(R.id.btn_return_to_home)
    }

    private fun setAllListener(){
        this.returnToHome.setOnClickListener{
            startActivity(Intent(activity, HomeActivity::class.java))
            activity?.finish()
        }
    }

    private fun setMcOrderId(){
        this.mcId.text = McOrder.id
    }

    private fun setMcName(){
        if(McOrder.location != null){
            this.mcName.text = McOrder.location!!.name
        }
    }

    private fun generateMcQrCode(){
        val qrGenerator = QRGenerator()
        this.mcQrCode.setImageBitmap(qrGenerator.generateQrFromString(McOrder.id))
    }

    override fun onResume() {
        super.onResume()

        if(activity != null){
            this.generateMcQrCode()
            this.setMcOrderId()
            this.setMcName()

            //Remove Order Info
            McOrder.cancelOrder()
        }
    }
}