package com.mayank.myapplication.mayank

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.mayank.myapplication.Activity.ForgotPassword
import com.mayank.myapplication.R
import kotlinx.android.synthetic.main.activity_registration.*

class LoginActivity : AppCompatActivity() {
    lateinit var mlogin     : Button
    lateinit var mEMail    :  EditText
    lateinit var mPassword :  EditText
    lateinit var mCreatebtn:  TextView
    lateinit var auth      :  FirebaseAuth
    lateinit var forgot     : TextView
    lateinit var progressBar:  ProgressBar
    lateinit var logo         : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.activity_login)
        mlogin     = findViewById(R.id.loginBtn)
        mPassword  = findViewById(R.id.password)
        mEMail     = findViewById(R.id.Email)
        forgot     = findViewById(R.id.forgotPassword)
        mCreatebtn = findViewById(R.id.createText)
        logo         = findViewById(R.id.logoprofile)
        auth       = FirebaseAuth.getInstance()
        progressBar= findViewById(R.id.progressBar)
        onsetup()
        create()
        forgot()
    }

    fun onsetup() {
        mlogin.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val Email = mEMail.text.toString()
                val Password = mPassword.text.toString()
                if (TextUtils.isEmpty(Email)) {
                    mEMail.error = "Email is Required."
                    return
                }
                if (TextUtils.isEmpty(Password)) {
                    mPassword.error = "Password is Required."
                    return
                }
                if (Password.length < 6) {
                    mPassword.error = "Password Must be >= 6 Characters"
                    return
                }
                progressBar.visibility = View.VISIBLE
                auth.signInWithEmailAndPassword(Email,Password).addOnCompleteListener{task ->
                    if(task.isSuccessful)
                    {
                        Toast.makeText(this@LoginActivity,"LoginSuccessful",Toast.LENGTH_LONG).show()
                        val intent =Intent(this@LoginActivity,
                            MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(this@LoginActivity,"Error ! " + (task.getException()?.message),Toast.LENGTH_LONG).show()
                        progressBar.visibility = View.GONE
                    }

                }
            }
       })
    }
    fun create()
    {
        mCreatebtn.setOnClickListener()
        {
            val intent =Intent(this@LoginActivity,
                Registration::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun forgot()
    {
        forgot.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val intent = Intent(
                    this@LoginActivity,
                    ForgotPassword::class.java
                )
                intent.putExtra("ActivityName","Login")
                startActivity(intent)
                finish()
            }
        })
    }
}