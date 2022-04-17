package com.example.mcdonalds.utils


import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mcdonalds.MainActivity
import com.example.mcdonalds.R

class FragmentUtils {

    companion object{
        fun changeCurrentFragment(activity: AppCompatActivity, fragment: Fragment, tag: String) : Unit {
            if(fragment != null){
                val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
                transaction.replace(R.id.fragment_container, fragment, tag)
                transaction.commit()
            }
        }

    }
}