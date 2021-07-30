package com.mayank.myapplication.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.TaskExecutors
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import kotlinx.android.synthetic.main.fragment_profile.*
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit


class Profile : Fragment() {
    lateinit var db: DocumentReference
    lateinit var profilename               : TextView
    lateinit var email                     : TextView
    lateinit var number                    : TextView
    lateinit var youtube                   : Button
    lateinit var facebooklink     : String
    lateinit var youtubelink      : String
    lateinit var instagramlink    : String
    lateinit var otp                       : EditText
    lateinit var verify                    : Button
    lateinit var logo                      : ImageView
    lateinit var facebook                  : Button
    lateinit var Instagram                 : Button
    lateinit var changepass                : Button
    lateinit var newpass                   : EditText
    lateinit var confirmpass               : EditText
    lateinit var userId                    : String
    lateinit var confirm                   : Button
    lateinit var phonenumber               : String
    lateinit var progress                  : ProgressBar
    lateinit var fstore                    : FirebaseFirestore
    private lateinit var verificationID: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        youtube                   = view.findViewById(R.id.btsubscribeyoutube)
        facebook                  = view.findViewById(R.id.btlikefacebook)
        Instagram                 = view.findViewById(R.id.btfollowInstagram)
        changepass                = view.findViewById(R.id.btchangepass)
        newpass                   = view.findViewById(R.id.etnewpass)
        confirmpass               = view.findViewById(R.id.etconfirmPass)
        confirm                   = view.findViewById(R.id.btconfirmpass)
        profilename               = view.findViewById(R.id.txtprofilename)
        logo                      = view.findViewById(R.id.logoprofile)
        progress                  = view.findViewById(R.id.progressBar)
        otp                       = view.findViewById(R.id.etOTP)
        verify                    = view.findViewById(R.id.btverify)
        email                     = view.findViewById(R.id.txtprofileemail)
        number                    = view.findViewById(R.id.txtprofilenumber)
        fstore                   = FirebaseFirestore.getInstance()
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
        db = fstore.collection("users").document(userId)
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("YourTag", "Listen failed.", error)
            }
            profilename.text = "Name : "+value?.getString("Name")
            email.text = "Email : " + value?.getString("email")
            number.text = "Number : " +value?.getString("phone")
            phonenumber = value?.getString("phone").toString()
        }
        FirebaseFirestore.getInstance().collection("socialmedia").document("links")
            .addSnapshotListener{value, error ->
                if (error != null) {
                    Log.w("tid", "Listen failed.", error)
                }
                facebooklink  = value?.getString("facebooklink").toString()
                youtubelink   = value?.getString("youtubelink").toString()
                instagramlink = value?.getString("instagramlink").toString()
                Log.d("Links","Successfully Added Social Media Links")
            }
        youtube.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubelink))
            startActivity(intent)
        }
        facebook.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(facebooklink))
            startActivity(intent)
        }
        Instagram.setOnClickListener()
        {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(instagramlink))
            startActivity(intent)
        }
        changepass.setOnClickListener()
        {
            profilename.visibility=View.GONE
            number.visibility=View.GONE
            email.visibility=View.GONE
            progress.visibility=View.GONE
            sendverificationcodetosuer(phonenumber)
            otp.visibility=View.VISIBLE
            verify.visibility=View.VISIBLE
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
        return view
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
                Toast.makeText(context, p0.message, Toast.LENGTH_LONG).show()
            }

        }

    private fun verifycode(codebyuser: String) {
        val credential = PhoneAuthProvider.getCredential(verificationID, codebyuser)
        signintheuser(credential)
    }

    private fun signintheuser(credential: PhoneAuthCredential) {
        FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                progress.visibility=View.GONE
                otp.visibility=View.GONE
                verify.visibility=View.GONE
                newpass.visibility=View.VISIBLE
                confirmpass.visibility=View.VISIBLE
                confirm.visibility = View.VISIBLE
                val a = newpass.text.toString()
                val b = confirmpass.text.toString()
                confirm.setOnClickListener()
                {
                    if(TextUtils.isEmpty(a))
                    {
                        newpass.error = "Enter New Password"
                        newpass.requestFocus()
                    }
                    if(TextUtils.isEmpty(b))
                    {
                        confirmpass.error = "Enter your password Again To confirm"
                        confirmpass.requestFocus()
                    }
                    if (a == b) {
                        progress.visibility=View.VISIBLE
                        db = FirebaseFirestore.getInstance().collection("users").document(userId)
                        val user = mutableMapOf<String, String>()
                        user["Password"] = newpass.text.toString()
                        db.update(user as Map<String, Any>).addOnSuccessListener {
                            Log.d(tag, "onSuccess: user Profile is created for $userId")
                        }
                        newpass.visibility=View.GONE
                        confirmpass.visibility=View.GONE
                        confirm.visibility=View.GONE
                        progress.visibility = View.GONE
                        profilename.visibility=View.VISIBLE
                        number.visibility=View.VISIBLE
                        email.visibility=View.VISIBLE
                    }
                    else
                    {
                        Toast.makeText(context,"Both Passwords Are not Same",Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}