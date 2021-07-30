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
import com.google.firebase.firestore.FirebaseFirestore
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import io.grpc.ConnectivityState
import kotlinx.android.synthetic.main.activity_registration.*
import java.util.*
import java.util.concurrent.TimeUnit

class VerifyUser : AppCompatActivity() {
    val tag: String = "TAG"
    lateinit var otp: EditText
    lateinit var verify: Button
    lateinit var auth: FirebaseAuth
    lateinit var progress: ProgressBar
    lateinit var UserID: String
    lateinit var fstore: FirebaseFirestore
    lateinit var email: String
    lateinit var phonenumber: String
    lateinit var pass        : String
    lateinit var name: String
    lateinit var logo         : ImageView
    private lateinit var verificationID: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_user)
        otp = findViewById(R.id.etOTP)
        logo         = findViewById(R.id.logoprofile)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        verify = findViewById(R.id.btverify)
        auth = FirebaseAuth.getInstance()
        auth.setLanguageCode(Locale.getDefault().language)
        fstore = FirebaseFirestore.getInstance()
        progress = findViewById(R.id.progressBar)
        if (intent != null) {
                name = intent.getStringExtra("Name")!!
                email = intent.getStringExtra("Email")!!
                pass = intent.getStringExtra("Password")!!
                phonenumber = intent.getStringExtra("Number")!!
        }
        progress.visibility=View.VISIBLE
            sendverificationcodetosuer(phonenumber)
        progress.visibility=View.GONE
        verify.setOnClickListener()
        {
            progress.visibility=View.VISIBLE
            val code = otp.text.toString()
            if(code.isEmpty()||code.length<6)
            {
                otp.error = "Wrong OTP ..."
                otp.requestFocus()
            }
            verifycode(code)
            progress.visibility=View.GONE
        }
        }
    private fun sendverificationcodetosuer(phonenumber: String) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            "+91"+phonenumber,
            5,
            TimeUnit.SECONDS,
            TaskExecutors.MAIN_THREAD,
            callbacks)
    }
    private val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks()
    {
        override fun onCodeSent(p0: String, p1: PhoneAuthProvider.ForceResendingToken) {
            verificationID = p0
        }
        override fun onVerificationCompleted(p0: PhoneAuthCredential) {
           val code = p0.smsCode
            if(code!=null)
            {
                progress.visibility = View.VISIBLE
                verifycode(code)
            }
        }

        override fun onVerificationFailed(p0: FirebaseException) {
            Toast.makeText(this@VerifyUser,p0.message,Toast.LENGTH_LONG).show()
        }

    }
    private fun verifycode(codebyuser:String)
    {
        val credential = PhoneAuthProvider.getCredential(verificationID,codebyuser)
        signintheuser(credential)
    }

    private fun signintheuser(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                auth.createUserWithEmailAndPassword(email, pass)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                                Toast.makeText(
                                    this@VerifyUser,
                                    "User is Created",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                                UserID = auth.currentUser!!.uid
                                val db = fstore.collection("users").document(UserID)
                                val user = mutableMapOf<String, String>()
                                user["Name"] = name
                                user["email"] = email
                                user["phone"] = phonenumber
                                user["Password"] = pass
                                user["pubgid"] = ""
                                user["pubgusername"] = ""
                                user["number"]="0"
                                db.set(user).addOnSuccessListener {
                                    Log.d(tag, "onSuccess: user Profile is created for $UserID")
                                }
                                val user1 = mutableMapOf<String, Int>()
                                user1["DepositCash"] = 0
                                user1["WinningCash"] = 0

                                FirebaseFirestore.getInstance().collection("Wallet")
                                    .document(UserID)
                                    .set(user1).addOnSuccessListener {
                                        Log.d(
                                            tag,
                                            "onSuccess: user Profile is created for $UserID"
                                        )
                                    }
                                val intent = Intent(this@VerifyUser, MainActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                Toast.makeText(
                                    this@VerifyUser,
                                    "Error ! " + (task.getException()?.message),
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                else
                {
                    Toast.makeText(this@VerifyUser, task.exception?.message, Toast.LENGTH_LONG)
                        .show()
                }
        }
    }
}