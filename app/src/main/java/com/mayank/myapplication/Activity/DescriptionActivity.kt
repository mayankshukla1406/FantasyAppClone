package com.mayank.myapplication.Activity

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.NonNull
import androidx.appcompat.widget.Toolbar
import androidx.core.view.marginStart
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.mayank.myapplication.R
import kotlinx.android.synthetic.main.activity_description.*
import kotlinx.android.synthetic.main.activity_login.*

class DescriptionActivity : AppCompatActivity() {
    lateinit var a                         : String
    lateinit var swipe                     : SwipeRefreshLayout
    lateinit var facebooklink              : String
    lateinit var youtubelink               : String
    lateinit var instagramlink             : String
    lateinit var b                         : String
    lateinit var db                        : DocumentReference
    lateinit var db1                       : DocumentReference
    lateinit var pubgid                    : EditText
    lateinit var pubgid1                   : TextView
    lateinit var btpubgusernamechange      : Button
    lateinit var okpubgusername            : Button
    lateinit var userid                    : String
    lateinit var fstore                    : FirebaseFirestore
    lateinit var tournamentName            : TextView
    lateinit var tournamentTime            : TextView
    lateinit var tournamentDate            : TextView
    lateinit var tournamentcount           : TextView
    lateinit var tournamenttotalcount      :TextView
    lateinit var tournamentAmount          : TextView
    lateinit var tournamentdesc            : TextView
    lateinit var tournamentImage           : ImageView
    lateinit var tournamentType            : TextView
    lateinit var btnJoinNow                : Button
    lateinit var btnJoined                 : Button
    lateinit var btnnotavailable           : Button
    lateinit var progressBar               : ProgressBar
    private lateinit var progressLayout    : RelativeLayout
    lateinit var tournamentDesc            : TextView
    lateinit var txtroom                   : TextView
    lateinit var txtroompassword           : TextView
    lateinit var youtube                   : Button
    lateinit var facebook                  : Button
    lateinit var Instagram                 : Button
    lateinit var winningprice              : String
    lateinit var winningprice2             : String
    lateinit var winningprice3             : String
    lateinit var killprice                 : String
    lateinit var roomid                    : String
    lateinit var type                      : String
    lateinit var roompassword              : String
    lateinit var count                     : String
    lateinit var count2                    : String
    var tournamentID                       : String? = "100"
    lateinit var toolbar                   : Toolbar
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        userid = FirebaseAuth.getInstance().currentUser?.uid.toString()
        fstore = FirebaseFirestore.getInstance()
        pubgid                    = findViewById(R.id.etpubgID)
        pubgid1                   = findViewById(R.id.txtpubgID)
        okpubgusername            = findViewById(R.id.btdoneusername)
        btpubgusernamechange      = findViewById(R.id.btchangeusername)
        tournamentName            = findViewById(R.id.txtTournamentName)
        tournamentTime            = findViewById(R.id.txtTournamentTime)
        tournamentDate            = findViewById(R.id.txtTournamentDate)
        tournamentAmount          = findViewById(R.id.txtTournamentAmount)
        tournamentType            = findViewById(R.id.txtTournamentType)
        tournamentcount           = findViewById(R.id.txttuserscount)
        tournamenttotalcount      = findViewById(R.id.txttusers)
        tournamentImage           = findViewById(R.id.imgTournament)
        tournamentDesc            = findViewById(R.id.txtTournamentDesc)
        btnJoinNow                = findViewById(R.id.btnJoinNow)
        btnJoined                 = findViewById(R.id.btnJoined)
        btnnotavailable           = findViewById(R.id.btnnotavailable)
        txtroom                   = findViewById(R.id.txtroomid)
        txtroompassword           = findViewById(R.id.txtroompassword)
        progressBar               = findViewById(R.id.progressBar)
        progressLayout            = findViewById(R.id.progressLayout)
        toolbar                   = findViewById(R.id.toolbar)
        youtube                   = findViewById(R.id.btsubscribeyoutube)
        facebook                  = findViewById(R.id.btlikefacebook)
        Instagram                 = findViewById(R.id.btfollowInstagram)
        progressBar.visibility    = View.VISIBLE
        progressLayout.visibility = View.VISIBLE
        btnJoinNow.visibility     = View.VISIBLE
        pubgid.visibility         = View.VISIBLE
        okpubgusername.visibility = View.VISIBLE
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tournament Details"
        if (intent != null) {
            tournamentID = intent.getStringExtra("tournamentID")
        }
        else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
        if (tournamentID == "100") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some unexpected error occurred!",
                Toast.LENGTH_SHORT
            ).show()
        }
            db = fstore.collection("tournamentlists")
                .document(tournamentID.toString())
            db.addSnapshotListener { value, error ->
                progressLayout.visibility = View.GONE
                if (error != null) {
                    Log.w("YourTag", "Listen failed.", error)
                }
                txtTournamentName.text      = value?.getString("tournamentName").toString()
                txtTournamentTime.text      = "Time:"+value?.getString("tournamentTime").toString()
                txtTournamentDate.text      = "Date:"+value?.getString("tournamentDate").toString()
                txtTournamentAmount.text    = value?.getString("tournamentAmount").toString()
                txtTournamentType.text      = value?.getString("tournamentType").toString()
                txtTournamentMap.text       = value?.getString("tournamentMap").toString()
                imgTournament.setImageResource(R.drawable.pubgback)
            }
        fstore.collection(tournamentID!!).document("counter")
            .addSnapshotListener { value, error ->
                if (error != null) {
                    Log.w("tid", "Listen failed.", error)
                }
                count            =  value?.getString("count").toString()
                count2           =  value?.getString("count2").toString()
                roomid           =  value?.getString("roomid").toString()
                roompassword     =  value?.getString("password").toString()
                winningprice     =  value?.getString("Winningprice").toString()
                winningprice2    =  value?.getString("Winningprice2").toString()
                winningprice3    =  value?.getString("Winningprice3").toString()
                killprice        =  value?.getString("killprice").toString()
                type             =  value?.getString("status").toString()
                if(count.length==1)
                {
                    txttuserscount.text = "00"+count
                }
                if(count.length==2)
                {
                    txttuserscount.text = "0"+count
                }
                if(count.length==3)
                {
                    txttuserscount.text = count
                }
                txttusers.text            = count2
                Log.d("Count value","onSucess Count value is $count")
                if(type=="notactive")
                {
                    btnnotavailable.visibility = View.VISIBLE
                    btnJoinNow.visibility      = View.GONE
                }
                if(txtTournamentType.text.toString()=="TDM")
                {
                    txtTournamentDesc.text =
                        "Winning Price ="+winningprice+"\n"+
                                "Player who has most number of Kills in the whole match" +
                                "is the winner from all of 8 players\n"+
                                "Note:\n" +
                                "1= Level of player must be above than 35.\n" +
                                "2= Headshot Percentage must be below than 35%. \n" +
                                "3= You must be above than Gold III Tier in Current Season.\n" +
                                "4=Do not use any type of Hacks in the game otherwise you will going to be disqualified at the end.\n"+
                                "5= All transactions are secured by Razorpay\n"+
                                "6=For any assistance reagarding any matter you can ask in ask section of app.\n"+
                                "7=You can message your problems on Instagram or on our Facebook Page.\n"+
                                "8=Follow us on Instagram and Like our Facebook "+
                                "Page and Subscribe our Youtube Channel.All links are given below."
                }
                else
                {
                    txtTournamentDesc.text =
                        "1st Rank Winning Price =" + winningprice + "\n" +
                                "2nd Rank Winning Price =" + winningprice2 + "\n" +
                                "3rd Rank Winning Price =" + winningprice3 + "\n" +
                                "Per kill =" + killprice + "\n" +
                                "Note:\n" +
                                "1= Level of player must be above than 35.\n" +
                                "2= Headshot Percentage must be below than 35%. \n" +
                                "3= You must be above than Gold III Tier in Current Season.\n" +
                                "4=Do not use any type of Hacks in the game otherwise you will going to be disqualified at the end.\n"+
                                "5= All transactions are secured by Razorpay\n"+
                                "6=For any assistance reagarding any matter you can ask in ask section of app.\n"+
                                "7=You can message your problems on Instagram or on our Facebook Page.\n"+
                                "8=Follow us on Instagram and Like our Facebook "+
                                "Page and Subscribe our Youtube Channel.All links are given below."
                }
            }
        val documentReference = fstore.collection(tournamentID!!).document(userid)
        documentReference.get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val documentSnapshot = task.result
                if (documentSnapshot != null && documentSnapshot.exists()) {
                    txtroom.text                    = "Room ID : $roomid"
                    txtroompassword.text            = "Room Password : $roompassword"
                    btnJoinNow.visibility           = View.GONE
                    btpubgusernamechange.visibility = View.GONE
                    btnJoined.visibility            = View.VISIBLE
                } else {
                    btnJoinNow.visibility=View.VISIBLE
                    btnJoined.visibility = View.GONE
                }
            }
        }
            .addOnFailureListener { e ->
                Toast.makeText(this@DescriptionActivity, e.message, Toast.LENGTH_LONG).show()
                Log.d("Androidview", e.message!!)
            }
        db1= fstore.collection("users").document(userid)
            db1.addSnapshotListener{value, error ->
                if(error!=null)
                {
                    Log.w("YourTag", "Listen failed.", error)
                }
                a = value?.getString("pubgid").toString()
                if(a!="")
                {
                    pubgid.visibility              = View.GONE
                    pubgid1.text = a
                    pubgid1.visibility                = View.VISIBLE
                    btpubgusernamechange.visibility   = View.VISIBLE
                    okpubgusername.visibility         =View.GONE
                }
            }
        btpubgusernamechange.setOnClickListener()
        {
            btpubgusernamechange.visibility   = View.GONE
            okpubgusername.visibility         = View.VISIBLE
            pubgid1.visibility             = View.GONE
            pubgid.visibility              = View.VISIBLE
        }
        okpubgusername.setOnClickListener()
        {
            if(TextUtils.isEmpty(pubgid.text.toString()))
            {
                pubgid.error = "Enter your Pubg Username"
            }
            else {
                val user = mutableMapOf<String, Any>()
                user["pubgid"] = pubgid.text.toString()
                db1.update(user).addOnCompleteListener {
                    Log.d("Pubguserid", "onSuccess: user pubgID is created for $userid")
                }
                pubgid1.visibility             = View.VISIBLE
                pubgid.visibility              = View.GONE
                btpubgusernamechange.visibility      = View.VISIBLE
                okpubgusername.visibility            = View.GONE
            }
        }
        fstore.collection("socialmedia").document("links")
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
            btnJoinNow.setOnClickListener()
            {
                if(a=="")
                {
                    Toast.makeText(this@DescriptionActivity,"You need to set your PubgId first to Join the tournament",Toast.LENGTH_LONG).show()
                }
                if(count=="100")
                {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "This Room is full. Join the Other tournament!",
                        Toast.LENGTH_LONG
                    ).show()
                }
                if(a!=""&&count!="100"){
                    val intent = Intent(this@DescriptionActivity, PaymentActivity::class.java)
                    intent.putExtra("AmountValue", txtTournamentAmount.text.toString())
                    intent.putExtra("describe", true)
                    intent.putExtra("tid", tournamentID)
                    startActivity(intent)
                }
            }
            btnJoined.setOnClickListener()
            {
                Toast.makeText(
                    this@DescriptionActivity,
                    "You already joined the Tournament",
                    Toast.LENGTH_LONG
                ).show()
            }
        btnnotavailable.setOnClickListener()
        {
            Toast.makeText(
                this@DescriptionActivity,
                "Not available to Enter Right Now. Come back after some time!",
                Toast.LENGTH_LONG
            ).show()
        }
        swipe = findViewById(R.id.swiperefresh)
        swipe.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener
        {
            override fun onRefresh() {
                swipe.isRefreshing=false
            }
        })
    }
}
