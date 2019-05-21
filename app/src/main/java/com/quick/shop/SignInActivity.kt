package com.quick.shop

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_in.*

class SignInActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        val sEmail = email.text.toString()
        val sPasswd = password.text.toString()
        signin.setOnClickListener { view ->
            FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(sEmail, sPasswd)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        AlertDialog.Builder(this@SignInActivity)
                            .setTitle("Sign In")
                            .setMessage("Account created")
                            .setPositiveButton("OK") { dialog, which ->
                                setResult(Activity.RESULT_OK)
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
}
