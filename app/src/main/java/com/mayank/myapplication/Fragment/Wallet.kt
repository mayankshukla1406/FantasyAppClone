package com.mayank.myapplication.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mayank.myapplication.Activity.PaymentActivity
import com.mayank.myapplication.Model.TournamentList
import com.mayank.myapplication.R
import kotlinx.android.synthetic.main.fragment_wallet.*

class Wallet : Fragment() {
    lateinit var youtube          : Button
    lateinit var facebook         : Button
    lateinit var Instagram        : Button
    lateinit var facebooklink     : String
    lateinit var youtubelink      : String
    lateinit var instagramlink    : String
    lateinit var winningCash      : TextView
    lateinit var DepositCash      : TextView
    lateinit var winningCash1     : TextView
    lateinit var DepositCash1     : TextView
    lateinit var withdraw         : Button
    lateinit var addcash          : Button
    lateinit var logo             : ImageView
    lateinit var db               : DocumentReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_wallet, container, false)
        youtube                   = view.findViewById(R.id.btsubscribeyoutube)
        facebook                  = view.findViewById(R.id.btlikefacebook)
        logo                      = view.findViewById(R.id.logoprofile)
        Instagram                 = view.findViewById(R.id.btfollowInstagram)
        val userid = FirebaseAuth.getInstance().currentUser!!.uid
        db = FirebaseFirestore.getInstance().collection("Wallet").document(userid)
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("YourTag", "Listen failed.", error)
            }
            val dcash = value?.get("DepositCash")
            val wcash = value?.get("WinningCash")
            winningCash1.text=wcash.toString()
            DepositCash1.text=dcash.toString()
        }
        winningCash = view.findViewById(R.id.txtwinningCash)
        winningCash1 = view.findViewById(R.id.txtwinningCash1)

        DepositCash = view.findViewById(R.id.txtdepositCash)
        DepositCash1 = view.findViewById(R.id.txtdepositCash1)
        withdraw = view.findViewById(R.id.btwithdraw)
        addcash = view.findViewById(R.id.btaddcash)
        addcash.setOnClickListener()
        {
            val intent = Intent(context, PaymentActivity::class.java)
            startActivity(intent)
        }
        withdraw.setOnClickListener()
        {
            val intent = Intent(context, PaymentActivity::class.java)

            startActivity(intent)
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
    return view
    }
}