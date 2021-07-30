package com.mayank.myapplication.Fragment

import android.app.Activity
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
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import com.squareup.picasso.Picasso
import io.grpc.Context

class Ask : Fragment() {
    lateinit var storage                   : StorageReference
    lateinit var youtube                   : Button
    lateinit var facebook                  : Button
    lateinit var Instagram                 : Button
    lateinit var ask                       : EditText
    lateinit var asktopic                  : EditText
    lateinit var submit                    : Button
    lateinit var userid                    : String
    lateinit var db                        : DocumentReference
    lateinit var dbd                       : DocumentReference
    lateinit var email                     : String
    lateinit var logo                      : ImageView
    lateinit var name                      : String
    lateinit var image                     : ImageView
    lateinit var facebooklink     : String
    lateinit var youtubelink      : String
    lateinit var instagramlink    : String
    lateinit var imageUri          : Uri
    lateinit var fstore                    : FirebaseFirestore
    var RequestCode=403
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_ask, container, false)
        image   = view.findViewById(R.id.imgcheck)
        fstore = FirebaseFirestore.getInstance()
        logo                      = view.findViewById(R.id.logoprofile)
        dbd  = fstore.collection("url").document("urlimage")
        userid = FirebaseAuth.getInstance().currentUser?.uid!!

        fstore.collection("users").document(userid)
            .addSnapshotListener{value, error ->
                if(error!=null)
                {
                    Log.w("YourTag", "Listen failed.", error)
                }
                email = value?.getString("email").toString()
                name = value?.getString("Name").toString()

            }
        youtube                   = view.findViewById(R.id.btsubscribeyoutube)
        facebook                  = view.findViewById(R.id.btlikefacebook)
        Instagram                 = view.findViewById(R.id.btfollowInstagram)
        ask                       = view.findViewById(R.id.etask)
        asktopic                  = view.findViewById(R.id.etasktopic)
        submit                    = view.findViewById(R.id.btsubmitask)
        submit.setOnClickListener()
        {
            db= fstore.collection("feedbacks").document(email)
             val user = mutableMapOf<String,String>()
            user.put(asktopic.text.toString(),ask.text.toString())
            db.update(user as Map<String, Any>).addOnCompleteListener{
                Log.d("TournamentRegistration", "onSuccess: Feedback registered given by $name")
            }
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