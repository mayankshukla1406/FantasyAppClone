package com.mayank.myapplication.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import kotlinx.android.synthetic.main.activity_forgot_password.*
import java.util.*
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

class ForgotPassword : AppCompatActivity() {
    private val tag = "String"
    lateinit var number: EditText
    lateinit var auth: FirebaseAuth
    lateinit var progress: ProgressBar
    lateinit var UserID: String
    lateinit var db: DocumentReference
    lateinit var fstore: FirebaseFirestore
    lateinit var confirmnumber: Button
    lateinit var otp: EditText
    lateinit var verify: Button
    lateinit var logo: ImageView
    private lateinit var verificationID: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        number = findViewById(R.id.etnumber)
        logo = findViewById(R.id.logoprofile)
        otp = findViewById(R.id.etOTP)
        confirmnumber = findViewById(R.id.btconfirmnumber)
        verify = findViewById(R.id.btverify)
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode(Locale.getDefault().language)
        fstore = FirebaseFirestore.getInstance()
        progress = findViewById(R.id.progressBar)
        UserID = auth.currentUser?.uid.toString()
        confirmnumber.setOnClickListener()
        {
            if(TextUtils.isEmpty(number.text.toString()))
            {
                number.error = "Please Enter a number to get OTP"
                number.requestFocus()
            }
            if(number.text.toString().length<10||number.text.toString().length>10)
            {
                number.error = "Please check your number and try again with 10 digit number!!"
                number.requestFocus()
            }
            else {
                progress.visibility = View.VISIBLE
                sendverificationcodetosuer(number.text.toString())
                progress.visibility = View.GONE
                otp.visibility = View.VISIBLE
                verify.visibility = View.VISIBLE
            }
        }
        verify.setOnClickListener()
        {
            progress.visibility = View.VISIBLE
            val code = otp.text.toString()
            if (code.isEmpty() || code.length < 6) {
                otp.error = "Wrong OTP ..."
                otp.requestFocus()
            }
            verifycode(code)
            progress.visibility = View.GONE
        }
    }

    private fun sendverificationcodetosuer(phonenumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91" + phonenumber,
            5,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks
        )
    }

    private val callbacks =
        object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
                verificationID = p0
            }

            override fun onVerificationCompleted(p0: PhoneAuthCredential) {
                val code = p0.smsCode
                if (code != null) {
                    progress.visibility = View.VISIBLE
                    verifycode(code)
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {
                Toast.makeText(this@ForgotPassword, p0.message, Toast.LENGTH_LONG).show()
            }

        }

    private fun verifycode(codebyuser: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, codebyuser)
        signintheuser(credential)
    }

    private fun signintheuser(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val intent = Intent(this@ForgotPassword, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}