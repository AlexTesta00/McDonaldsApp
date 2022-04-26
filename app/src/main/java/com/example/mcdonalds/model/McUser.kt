package com.example.mcdonalds.model

class McUser (private val name : String, private val surname : String, private val email : String) : User {

    private var logged : Boolean = false

    override fun getName(): String {
        return this.name
    }

    override fun getSurname(): String {
        return this.surname
    }

    override fun getEmail(): String {
        return this.email
    }

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