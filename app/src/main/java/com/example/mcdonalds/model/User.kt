package com.example.mcdonalds.model

interface User {
    fun getName() : String
    fun getSurname() : String
    fun getEmail() : String
    fun registerUser() : Void
    fun loginUser() : Void
    fun logOutUser() : Void
    fun isLogged() : Boolean
}