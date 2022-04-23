package com.example.mcdonalds.model

interface User {
    fun registerUser() : Void
    fun loginUser() : Void
    fun logOutUser() : Void
    fun isLogged() : Boolean
}