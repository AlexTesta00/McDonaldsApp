package com.example.mcdonalds.utils


import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.example.mcdonalds.R

class FragmentUtils {

    companion object{
        fun changeCurrentFragment(activity: AppCompatActivity, fragment: Fragment, tag: String){
            val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            transaction.replace(R.id.fragment_container, fragment, tag)
            transaction.addToBackStack(tag)
            transaction.commit()
        }

        fun changeAppBarName(activity: AppCompatActivity, title: String){
            activity.findViewById<AppCompatTextView>(R.id.app_bar_title).text = title
        }

        fun changeToFinishFragment(activity: AppCompatActivity, fragment: Fragment, tag: String){
            val transaction:FragmentTransaction = activity.supportFragmentManager.beginTransaction()
            transaction.setCustomAnimations(R.anim.slide_in, R.anim.fade_out, R.anim.fade_in, R.anim.slide_out)
            transaction.replace(R.id.fragment_map_container, fragment, tag)
            transaction.commit()
        }

        fun returnToBackFragment(activity: AppCompatActivity){
            activity.supportFragmentManager.popBackStack()
        }

    }
}