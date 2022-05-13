package com.example.mcdonalds

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleButton : SignInButton

    private companion object{
        private const val RC_SIGN_IN = 100
        private const val TAG = "GOOGLE_SIGN_IN"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        this.bindComponents()
        this.setAllListener()

        //Configure GoogleSignIn
        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail() //get user email
            .requestId() //get user id
            .build()

        googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)

        //init firebase auth
        firebaseAuth = FirebaseAuth.getInstance()
        checkUser()

    }

    private fun checkUser() {
        //Check if user is logged in
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){
            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
            finish()
        }
    }

    private fun bindComponents() {
        this.googleButton = findViewById(R.id.btn_google)
    }

    private fun setAllListener(){
        this.googleButton.setOnClickListener{
            Log.d(TAG, "onCreate: GoogleSignIn")
            startActivityForResult(Intent(googleSignInClient.signInIntent), RC_SIGN_IN)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        //Result launched from sign-in intent
        if(requestCode == RC_SIGN_IN){
            Log.d(TAG, "OnActivityResult: google sign-in activity result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                //Sign-in success, auth whit firebase
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWhitGoogleAccount(account)
            }catch (e: Exception){
                Log.d(TAG, "OnActivityResult: ${e.message} failed")
            }
        }
    }

    private fun firebaseAuthWhitGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firAccountGoogle: begin firebase google")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnSuccessListener {
                Log.d(TAG, "firAccountGoogle: LoggedIn")

                //Get logged User
                val firebaseUser = firebaseAuth.currentUser
                val uid = firebaseAuth!!.uid
                val email = firebaseUser!!.email

                Log.d(TAG, "firAccountGoogle: Email : $email")
                Log.d(TAG, "firAccountGoogle: Uid : $uid")

                //If is new user => create account
                if(it.additionalUserInfo!!.isNewUser){
                    Log.d(TAG, "firAccountGoogle: Account Created")
                    Log.d(TAG, "firAccountGoogle: Email : $email")
                    Log.d(TAG, "firAccountGoogle: Uid : $uid")

                    //Display User
                    Toast.makeText(this@LoginActivity, "Grazie, $email per esserti registrato", Toast.LENGTH_SHORT).show()
                }else{
                    //User Exist
                    Toast.makeText(this@LoginActivity, "Bentornato, $email", Toast.LENGTH_SHORT).show()
                }

                //Start Main Activity
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()

            }.addOnFailureListener{
                Log.d(TAG, "firAccountGoogle: Loggin Failed ${it.message}")
                Toast.makeText(this@LoginActivity, "Il login non è andato a buon fine", Toast.LENGTH_SHORT).show()
            }
    }

    override fun onStart() {
        super.onStart()
    }

}