package com.quick.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        signUp.setOnClickListener { view ->
            signUp()
        }
        login.setOnClickListener {
            FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email.text.toString(),
                password.text.toString())
                .addOnCompleteListener{ task ->

                }
        }
    }

    private fun signUp() {
        val sEmail = email.text.toString()
        val sPasswd = password.text.toString()
        FirebaseAuth.getInstance()
            .createUserWithEmailAndPassword(sEmail, sPasswd)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign In")
                        .setMessage("Account created")
                        .setPositiveButton("OK") { dialog, which ->
                            setResult(RESULT_OK)
                            finish()
                        }.show()
                } else {
                    AlertDialog.Builder(this@SignInActivity)
                        .setTitle("Sign In")
                        .setMessage(task.exception?.message)
                        .setPositiveButton("OK", null)
                        .show()
                }
            }
    }
}
