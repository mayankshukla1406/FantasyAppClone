package com.mayank.myapplication.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import kotlinx.android.synthetic.main.activity_payment.*
import org.json.JSONObject
import com.razorpay.PaymentData
class PaymentActivity : AppCompatActivity(),PaymentResultListener {
    lateinit var addcash  : EditText
    lateinit var ch       : String
    lateinit var txt      : TextView
    lateinit var btadd    : Button
    lateinit var userid  : String
    lateinit var dcash   : String
    lateinit var wcash   : String
    lateinit var txtt    : TextView
    lateinit var withdraw: Button
    lateinit var btjoin  : Button
    lateinit var db      : DocumentReference
    lateinit var db1     : DocumentReference
    lateinit var fstore  : FirebaseFirestore
    lateinit var name    : String
    lateinit var pubgid  : String
    lateinit var email   : String
    lateinit var number  : String
    lateinit var logo         : ImageView
    lateinit var count       : String
    private val TAG = PaymentActivity::class.java.simpleName
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_payment)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        fstore = FirebaseFirestore.getInstance()
        userid = FirebaseAuth.getInstance().currentUser!!.uid
        addcash = findViewById(R.id.etaddcash)
        btadd = findViewById(R.id.btaddcash1)
        logo         = findViewById(R.id.logoprofile)
        btjoin = findViewById(R.id.btjoinTournament)
        withdraw = findViewById(R.id.btwithdraw1)
        db1=fstore.collection("users").document(userid)
        db1.addSnapshotListener{value, error ->
            if (error != null) {
                Log.w("YourTag", "Listen failed.", error)
            }
            email = value?.getString("email").toString()
            number = value?.getString("phone").toString()
        }
        db = fstore.collection("Wallet").document(userid)
        fstore.collection("users").document(userid)
        .addSnapshotListener{ value, error->if(error!=null){Log.w("tid", "Listen failed.", error) }
            name = value?.getString("Name").toString()
            pubgid = value?.getString("pubgid").toString()
        }
        Checkout.preload(applicationContext)
        btadd.setOnClickListener()
        {
            if (TextUtils.isEmpty(etaddcash.text.toString())) {
                Toast.makeText(
                    this@PaymentActivity,
                    "You have to enter a value to Add Money",
                    Toast.LENGTH_LONG
                ).show()
            }
            else {
                razorpay()

            }
        }
        withdraw.setOnClickListener()
        {
            if (TextUtils.isEmpty(etaddcash.text.toString())) {
                etaddcash.error = "Enter something to withdraw"
                etaddcash.requestFocus()
            }
            if(etaddcash.text.toString().toInt()<50)
            {
                Toast.makeText(
                    this@PaymentActivity,
                    "Withdraw Amount must be greater than 50",
                    Toast.LENGTH_LONG
                ).show()
            }
            if(etaddcash.text.toString().toInt()>(dcash.toInt()+wcash.toInt()))
            {
                Toast.makeText(
                    this@PaymentActivity,
                    "You don't have that money to withdraw",
                    Toast.LENGTH_LONG
                ).show()
            }
            else
            {
                val intent = Intent(this@PaymentActivity, Withdraw::class.java)
                intent.putExtra("Amount", etaddcash.text.toString())
                startActivity(intent)
            }
        }
        if(intent.getBooleanExtra("describe", false))
        {
            val tid = intent.getStringExtra("tid")!!
            fstore.collection(tid).document("counter")
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w("tid", "Listen failed.", error)
                    }
                    count = value?.getString("count").toString()
                }
            btjoin.visibility = View.VISIBLE
            addcash.text =
                Editable.Factory.getInstance().newEditable(intent.getStringExtra("AmountValue"))
            addcash.isEnabled=false
            btjoin.setOnClickListener()
            {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Do You want to join the Tournament!")
                builder.setPositiveButton("Yes") { dialog, which ->
                    if(etaddcash.text.toString().toInt()<=dcash.toInt()||etaddcash.text.toString().toInt()<=wcash.toInt()) {
                        onupdate()
                        val user = mutableMapOf<String, String>()
                        user["Name"] = name
                        user["PubgID"] = pubgid
                        fstore.collection(tid).document(userid)
                            .set(user).addOnSuccessListener {
                                Log.d(
                                    "registered",
                                    "onSuccess: user Profile is register with $userid"
                                )
                            }
                        val a = (count.toInt() + 1).toString()
                        val user2 = mutableMapOf<String, Any>()
                        user2["count"] = a
                        fstore.collection(tid).document("counter")
                            .update(user2).addOnSuccessListener {
                                Log.d(
                                    "registered",
                                    "onSuccess: user Profile is register with $userid"
                                )
                            }
                        val intent = Intent(this@PaymentActivity, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else
                    {
                        Toast.makeText(
                            this@PaymentActivity,
                            "You don't have Money in your wallet to join this Tournament",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                builder.setNegativeButton("No"){ dialog, which ->
                    val intent = Intent(this@PaymentActivity, MainActivity::class.java)
                    startActivity(intent)
                }
                val alertDialog : AlertDialog = builder.create()
                alertDialog.show()
            }
        }
        db.addSnapshotListener{ value, error ->
            if(error!=null)
            {
                Log.w("YourTag", "Listen failed.", error)
            }
            dcash = value?.get("DepositCash").toString()
            txt.text = "DepositCash : "+dcash
            wcash= value?.get("WinningCash").toString()
            txtt.text = "WinningCash : "+wcash
        }
        txtt = findViewById(R.id.txt)
        txt = findViewById(R.id.txt1)
    }
    override fun onPaymentError(errorCode: Int, response: String?) {
        try{
            Toast.makeText(this,"Payment failed $errorCode \n $response",Toast.LENGTH_LONG).show()
        }catch (e: Exception){
            Log.e(TAG,"Exception in onPaymentSuccess", e)
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        try {
            fstore.collection("Orders").document(userid)
                .get().addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val documentSnapshot = task.result
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            fstore.collection("Orders").document(userid).addSnapshotListener{value, error ->
                                if(error!=null)
                                {
                                    Log.d("tag","Exception in Success")
                                }
                                ch=value?.getString("count").toString()
                            }
                            val id = mutableMapOf<String,String>()
                            id["PaymentID"+ch]=razorpayPaymentId.toString()
                            id["count"]=(ch.toInt()+1).toString()
                            fstore.collection("Orders").document(userid).set(
                                id
                            ).addOnSuccessListener {
                                Log.d("tag","Successfully Order Set in Database")
                            }
                        }
                    }
                    else
                    {
                        val id = mutableMapOf<String,String>()
                        id["PaymentID"]=razorpayPaymentId.toString()
                        id["count"]=1.toString()
                        fstore.collection("Orders").document(userid).set(
                            id
                        ).addOnSuccessListener {
                            Log.d("tag","Successfully Order Set in Database")
                        }
                    }
                }
                .addOnFailureListener()
                {
                    Toast.makeText(this@PaymentActivity, "TransactionFailed", Toast.LENGTH_LONG).show()
                    Log.d("Androidview", "TransactionFailed")
                }
            Toast.makeText(this, "Payment Successful $razorpayPaymentId", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Log.e(TAG, "Exception in onPaymentSuccess", e)
        }
        onAdd()
    }
    fun onAdd()
    {
        val user1 = mutableMapOf<String, Int>()
        dcash = ((dcash.toInt()) + (etaddcash.text.toString().toInt())).toString()
        user1["DepositCash"] = dcash.toInt()
        user1["WinningCash"] = wcash.toInt()
        db.update(user1 as Map<String, Any>).addOnSuccessListener {
            Log.d("my tag", "onSuccess: user Profile is created for $userid")
        }
    }
    fun onupdate()
    {
        val add = etaddcash.text.toString().toInt()
            if (add<=dcash.toInt()&&dcash.toInt() > 0) {
                dcash = ((dcash.toInt()) - (add)).toString()
            } else if(etaddcash.text.toString().toInt()<=wcash.toInt()&&wcash.toInt()>0) {
                wcash = ((wcash.toInt()) - (add)).toString()
            }
        else{
                Toast.makeText(
                    this@PaymentActivity,
                    "You don't have Enough Money",
                    Toast.LENGTH_LONG
                ).show()
            }
        val user1 = mutableMapOf<String, Int>()
        user1["DepositCash"] = dcash.toInt()
        user1["WinningCash"] = wcash.toInt()
        db.update(user1 as Map<String, Any>).addOnSuccessListener {
            Log.d("your tag", "onSuccess: user Profile is changed for $userid")
        }
    }
   fun razorpay()
   {
       val razorpaypayment = (etaddcash.text.toString().toInt() *100).toString()
           /*
           *  You need to pass current activity in order to let Razorpay create CheckoutActivity
           * */
           val activity: Activity = this
           val co = Checkout()

           try {
               val options = JSONObject()
               options.put("name", "Players Point")
               options.put("description", "Amount to be Paid")
               //You can omit the image option to fetch the image from dashboard
               options.put("image", R.mipmap.ic_launcher)
               options.put("theme.color", "#3399cc")
               options.put("currency", "INR")
               options.put("payment_capture",true)
               options.put("amount", razorpaypayment)//pass amount in currency subunits
               val prefill = JSONObject()
               prefill.put("email", email)
               prefill.put("contact", number)
               options.put("prefill", prefill)
               co.open(activity, options)
           }catch (e: Exception){
               Toast.makeText(activity, "Error in payment: " + e.message, Toast.LENGTH_LONG).show()
               e.printStackTrace()
           }
       }
}