package com.example.mcdonalds.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.mcdonalds.R
import com.example.mcdonalds.model.McOrder
import com.example.mcdonalds.model.McUser
import com.example.mcdonalds.utils.MessageManager
import com.example.mcdonalds.utils.Permission
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import java.lang.Exception


class LoginActivity : AppCompatActivity() {

    private lateinit var googleSignInClient : GoogleSignInClient
    private lateinit var firebaseAuth: FirebaseAuth
    private lateinit var googleButton : SignInButton

    private companion object{
        private const val TAG = "GOOGLE_SIGN_IN"
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if(!Permission.checkNetworkIsEnabled(this@LoginActivity)){
            MessageManager.displayNoNetworkEnabled(this@LoginActivity)
        }else{
            this.bindComponents()
            this.setAllListener()

            //Configure GoogleSignIn
            val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_id))
                .requestEmail() //get user email
                .build()

            googleSignInClient = GoogleSignIn.getClient(this@LoginActivity, googleSignInOptions)

            //init firebase auth
            firebaseAuth = FirebaseAuth.getInstance()
            checkUser()
        }
    }

    private fun checkUser() {
        //Check if user is logged in
        val firebaseUser = firebaseAuth.currentUser
        if(firebaseUser != null){

            //Set User to order
            McOrder()

            //Start the Home activity
            startActivity(Intent(this, HomeActivity::class.java))
            this.finish()

        }
    }

    private fun bindComponents() {
        this.googleButton = findViewById(R.id.btn_google)
    }

    private fun setAllListener(){
        this.googleButton.setOnClickListener{
            if(!Permission.checkNetworkIsEnabled(this@LoginActivity)){
                MessageManager.displayNoNetworkEnabled(this@LoginActivity)
            }else{
                Log.d(TAG, "onCreate: GoogleSignIn")
                singInResult.launch(Intent(googleSignInClient.signInIntent))
            }
        }
    }

    private var singInResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
            Log.d(TAG, "${it.resultCode}")
        if(it.resultCode == Activity.RESULT_OK){
            Log.d(TAG, "OnActivityResult: google sign-in activity result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(it.data)
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
                val uid = firebaseAuth.uid
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

                //Set the user in current order
                McOrder.user = McUser(email!!, uid!!)
                Log.d("user", "Welcome $email")

                //Start Main Activity
                startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                finish()

            }.addOnFailureListener{
                Log.d(TAG, "firAccountGoogle: Loggin Failed ${it.message}")
                Toast.makeText(this@LoginActivity, "Il login non è andato a buon fine", Toast.LENGTH_SHORT).show()
            }
    }

}