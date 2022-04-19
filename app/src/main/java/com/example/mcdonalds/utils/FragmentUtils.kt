package com.example.mcdonalds.utils


import android.app.Activity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mcdonalds.MainActivity
import com.example.mcdonalds.R
import java.lang.IllegalStateException

class FragmentUtils {

    companion object{
        fun changeCurrentFragment(activity: AppCompatActivity, fragment: Fragment, tag: String){
            if(fragment != null){
                val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment, tag)
                transaction.commit()
            }
        }

        fun changeAppBarName(activity: Activity, title: String){
            if(activity != null){
                activity.findViewById<AppCompatTextView>(R.id.app_bar_title).text = title
            }else{
                throw IllegalStateException("Activity must be nothing")
            }
        }

    }
}