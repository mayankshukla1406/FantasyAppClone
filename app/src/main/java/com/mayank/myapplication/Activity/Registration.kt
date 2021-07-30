package com.mayank.myapplication.mayank

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mayank.myapplication.Activity.VerifyUser
import com.mayank.myapplication.R
import kotlinx.android.synthetic.main.activity_registration.*
class Registration : AppCompatActivity() {
    val tag = "TAG"
    lateinit var fullname: EditText
    lateinit var Email: EditText
    lateinit var password: EditText
    lateinit var number: EditText
    lateinit var signup: Button
    lateinit var login: TextView
    lateinit var progress: ProgressBar
    lateinit var auth  : FirebaseAuth
    lateinit var name           : String
    lateinit var phonenumber    : String
    lateinit var email          : String
    lateinit var pass           : String
    lateinit var logo         : ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        fullname = findViewById(R.id.fullName)
        Email = findViewById(R.id.Email)
        logo         = findViewById(R.id.logoprofile)
        password = findViewById(R.id.password)
        number = findViewById(R.id.phone)
        progress = findViewById(R.id.progressBar)
        signup = findViewById(R.id.registerBtn)
        login = findViewById(R.id.createText)
        auth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this@Registration, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
        onsetup()
        log()
    }

    fun onsetup() {
        signup.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                name = fullname.text.toString()
                email = Email.text.toString()
                pass = password.text.toString()
                phonenumber = number.text.toString()
                if (TextUtils.isEmpty(email)) {
                    Email.error = "Email is Required."
                    return
                }
                if (TextUtils.isEmpty(name)) {
                    fullname.error = "Name is Required"
                    return
                }
                if (TextUtils.isEmpty(pass)) {
                    password.error = "Password is Required."
                    return
                }
                if (TextUtils.isEmpty(phonenumber)) {
                    number.error = "Number is Required"
                }

                if (pass.length < 6) {
                    password.error = "Password Must be >= 6 Characters"
                    return
                }
                progressBar.visibility = View.VISIBLE
                val intent = Intent(this@Registration, VerifyUser::class.java)
                intent.putExtra("ActivityName","Registration")
                intent.putExtra("Name",name)
                intent.putExtra("Email",email)
                intent.putExtra("Number",phonenumber)
                intent.putExtra("Password",pass)
                startActivity(intent)
                finish()
                progressBar.visibility = View.GONE

            }
        }
        )
    }
    fun log()
    {
        login.setOnClickListener()
        {
            val intent = Intent(this@Registration,
                LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
