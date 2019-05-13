package com.quick.shop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signup.setOnClickListener {
            val sEmail = email.text.toString()
            val sPassword = passwrod.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail, sPassword)
        }
    }
}
