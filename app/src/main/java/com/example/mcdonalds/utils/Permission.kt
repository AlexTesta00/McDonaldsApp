package com.example.mcdonalds.utils

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities

class Permission {

    companion object{

        fun checkNetworkIsEnabled(context: Context) : Boolean{
            val connectivityManager : ConnectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val network = connectivityManager.activeNetwork ?: return false
            val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        }
    }
}