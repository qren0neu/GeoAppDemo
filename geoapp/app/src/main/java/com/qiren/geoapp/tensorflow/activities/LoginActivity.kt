package com.qiren.geoapp.tensorflow.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.qiren.geoapp.R
import com.qiren.geoapp.tensorflow.MyApp
import com.qiren.geoapp.tensorflow.beans.User
import com.qiren.geoapp.tensorflow.services.UserService

class LoginActivity : FragmentActivity() {

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var googleSignInOptions: GoogleSignInOptions

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        // Build a GoogleSignInClient with the options specified by googleSignInOptions.
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)

        // Set up sign-in button click listeners
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setOnClickListener { signIn() }
    }

    private fun signIn() {
        // check already logged in here


        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                if (account == null) {
                    onLoginFailed()
                    return
                }
                val user = User()
                user.email = account.email
                user.id = account.id
                user.token = account.idToken
                UserService.storeUser(user)
                firebaseAuthWithGoogle(account.idToken!!)
                finish()
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    private fun onLoginFailed() {
        Toast.makeText(MyApp.instance, "Google Auth Failed, Try Again", Toast.LENGTH_SHORT).show();
        finish()
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        // TODO: Authenticate with Firebase using the idToken
    }

    companion object {
        private const val TAG = "LoginActivity"
        private const val RC_SIGN_IN = 9001
    }
}
