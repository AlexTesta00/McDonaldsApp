package com.example.mcdonalds.model

class McUser (val name : String, val surname : String, val email : String) : User {

    private var logged : Boolean = false

    override fun registerUser(): Void {
        TODO("Not yet implemented")
    }

    override fun loginUser(): Void {
        TODO("Not yet implemented")
    }

    override fun logOutUser(): Void {
        TODO("Not yet implemented")
    }

    override fun isLogged(): Boolean {
        TODO("Not yet implemented")
    }
}