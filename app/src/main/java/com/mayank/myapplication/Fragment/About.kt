package com.mayank.myapplication.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.mayank.myapplication.R
import kotlinx.android.synthetic.main.activity_description.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.LocalDateTime.parse
import java.time.LocalTime
import java.util.*
import java.util.logging.Level.parse

class About : Fragment(){
    lateinit var youtube                   : Button
    lateinit var facebook                  : Button
    lateinit var Instagram                 : Button
    lateinit var logo                      : ImageView
    lateinit var About                     : TextView
    lateinit var About1                     : TextView
    lateinit var About2                     : TextView
    lateinit var facebooklink              : String
    lateinit var youtubelink               : String
    lateinit var instagramlink             : String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        val a : String = "Welcome to Players Point , a place where you can earn money " +
                         "by just playing games. So if you have a habit of playing " +
                "mobile games then why not make some money from that habit. " +
                "So enjoy"
        val b : String = "All the transactions of this app is done " +
                "by using the Razorpay Payment Gateway which is secure. " +
                "You guys can check about razorpay company.Do not worry about " +
                "your transactions."
        val c : String = "For any assistance reagarding any matter you can ask " +
                "in ask section of app. Also you can message us on Instagram or on " +
                "our Facebook Page. And Follow us on Instagram and Like our Facebook " +
                "Page and Subscribe our Youtube Channel.All links are given below. "
        About  = view.findViewById(R.id.txtabout)
        About1 = view.findViewById(R.id.txtabout1)
        About2 = view.findViewById(R.id.txtabout2)
        youtube                   = view.findViewById(R.id.btsubscribeyoutube)
        logo                      = view.findViewById(R.id.logoprofile)
        facebook                  = view.findViewById(R.id.btlikefacebook)
        Instagram                 = view.findViewById(R.id.btfollowInstagram)
        About.text = a
        About1.text= b
        About2.text= c
        val db = FirebaseFirestore.getInstance().collection("socialmedia").document("links")
            db.addSnapshotListener{value, error ->
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
        return view
    }
}