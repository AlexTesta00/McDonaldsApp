package com.example.mcdonalds.model

interface Location {
    fun getLatitude() : Double
    fun getLongitude() : Double
    fun getMcName() : String
    fun isOpen() : Boolean
}