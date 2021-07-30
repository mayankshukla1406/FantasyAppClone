package com.mayank.myapplication.Activity

import android.bluetooth.BluetoothAssignedNumbers
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mayank.myapplication.R
import com.mayank.myapplication.mayank.MainActivity
import kotlinx.android.synthetic.main.activity_payment.*
import kotlinx.android.synthetic.main.activity_withdraw.*

class Withdraw : AppCompatActivity() {
    lateinit var number              : String
    lateinit var upi                 : EditText
    lateinit var BAN                 : EditText
    lateinit var CBAN                : EditText
    lateinit var BANu                : EditText
    lateinit var IFSCCode            : EditText
    lateinit var toast               : TextView
    lateinit var confirmupi          : Button
    lateinit var confirmbank         : Button
    lateinit var textforselect       :TextView
    lateinit var selectUPI           : Button
    lateinit var logo                : ImageView
    lateinit var amount              : String
    lateinit var selectBankAccount   :Button
    lateinit var userid      : String
    lateinit var fstore      : FirebaseFirestore
    lateinit var db          : DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_withdraw)
        BAN = findViewById(R.id.etBAN)
        CBAN = findViewById(R.id.etconfirmBANu)
        BANu = findViewById(R.id.etBANu)
        toast = findViewById(R.id.txtRequestWithdraw)
        IFSCCode = findViewById(R.id.etIFSC)
        confirmbank = findViewById(R.id.btconfirbankaccount)
        toast = findViewById(R.id.txtRequestWithdraw)
        textforselect = findViewById(R.id.textforselect)
        selectUPI = findViewById(R.id.btUPIselect)
        selectBankAccount = findViewById(R.id.btbankaccountselect)
        logo = findViewById(R.id.logoprofile)
        upi = findViewById(R.id.etUPI)
        confirmupi = findViewById(R.id.btconfirmupi)
        fstore = FirebaseFirestore.getInstance()
        userid = FirebaseAuth.getInstance().currentUser!!.uid
        if (intent != null) {
            amount = intent.getStringExtra("Amount")!!
        }
            fstore.collection("users").document(userid)
                .addSnapshotListener { value, error ->
                    if (error != null) {
                        Log.w("YourTag", "Listen failed.", error)
                    }
                    number = value?.getString("number").toString()
                }
            db = fstore.collection("BankAccount").document(userid)
            db.get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val documentSnapshot = task.result
                    if (documentSnapshot != null && documentSnapshot.exists()) {
                        Toast.makeText(this, "exists", Toast.LENGTH_LONG).show()
                        txtRequestWithdraw.visibility = View.VISIBLE
                        val n = (number.toInt() + 1).toString()
                        val user = mutableMapOf<String, String>()
                        user["Amount" + n] = amount
                        fstore.collection("Withdrawals").document(userid)
                            .update(user as Map<String, Any>).addOnSuccessListener {
                                Log.d(
                                    "Amount updated",
                                    "onSuccess: Withdraw Request Successfully updated"
                                )
                            }
                        val user2 = mutableMapOf<String, String>()
                        user2["number"] = n
                        fstore.collection("users").document(userid)
                            .update(user2 as Map<String, Any>).addOnSuccessListener {
                                Log.d("Increment", "onSucces : Number is incremented successfully")
                            }
                        val withdrawintent = Intent(this@Withdraw, MainActivity::class.java)
                        Thread.sleep(5000)
                        startActivity(withdrawintent)
                        finish()
                    } else {
                        textforselect.visibility = View.VISIBLE
                        btUPIselect.visibility = View.VISIBLE
                        btbankaccountselect.visibility = View.VISIBLE
                        Toast.makeText(this, "notexists", Toast.LENGTH_LONG).show()
                        btbankaccountselect.setOnClickListener {
                            textforselect.visibility = View.GONE
                            btbankaccountselect.visibility = View.GONE
                            btUPIselect.visibility = View.GONE
                            etBAN.visibility = View.VISIBLE
                            etBANu.visibility = View.VISIBLE
                            etIFSC.visibility = View.VISIBLE
                            etconfirmBANu.visibility = View.VISIBLE
                            btconfirbankaccount.visibility = View.VISIBLE
                            confirmbank.setOnClickListener {
                                if (TextUtils.isEmpty(etconfirmBANu.text.toString())) {
                                    Toast.makeText(
                                        this@Withdraw,
                                        "You have enter Confirmed Bank Account Number",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (TextUtils.isEmpty(etBANu.text.toString())) {
                                    Toast.makeText(
                                        this@Withdraw,
                                        "You have to enter Bank Account Number",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (TextUtils.isEmpty(etBAN.text.toString())) {
                                    Toast.makeText(
                                        this@Withdraw,
                                        "You have to enter Name",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                if (etBANu.text.toString() == etconfirmBANu.text.toString()) {
                                    val user = mutableMapOf<String, Any>()
                                    user["BankAccountNumber"] = etBANu.text.toString()
                                    user["BankAccountHolderName"] = etBAN.text.toString()
                                    user["IFSC Code"] = etIFSC.text.toString()
                                    fstore.collection("BankAccount").document(userid)
                                        .set(user).addOnSuccessListener {
                                            Log.d(
                                                "Account Details",
                                                "onSuccess: Account details are saved"
                                            )
                                        }
                                    val user2 = mutableMapOf<String, String>()
                                    user2["Amount"] = amount
                                    fstore.collection("Withdrawals").document(userid)
                                        .set(user2).addOnSuccessListener {
                                            Log.d(
                                                "Amountrequested",
                                                "onSuccess: Amount requested saved"
                                            )
                                        }
                                    etBAN.visibility = View.GONE
                                    etBANu.visibility = View.GONE
                                    etIFSC.visibility = View.GONE
                                    etconfirmBANu.visibility = View.GONE
                                    btconfirbankaccount.visibility = View.GONE
                                    txtRequestWithdraw.visibility = View.VISIBLE
                                    Thread.sleep(5000)
                                    val withdrawintent =
                                        Intent(this@Withdraw, MainActivity::class.java)
                                    startActivity(withdrawintent)
                                    finish()
                                } else {
                                    Toast.makeText(
                                        this@Withdraw,
                                        "Account Numbers are different",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                            }
                        }
                        btUPIselect.setOnClickListener {
                            textforselect.visibility = View.GONE
                            btbankaccountselect.visibility = View.GONE
                            btUPIselect.visibility = View.GONE
                            etUPI.visibility = View.VISIBLE
                            btconfirmupi.visibility = View.VISIBLE
                            confirmupi.setOnClickListener {
                                if (TextUtils.isEmpty(etUPI.text.toString())) {
                                    Toast.makeText(
                                        this@Withdraw,
                                        "You have to enter a value to Add Money",
                                        Toast.LENGTH_LONG
                                    ).show()
                                } else {
                                    val user = mutableMapOf<String, String>()
                                    user["upiid"] = etUPI.text.toString()
                                    fstore.collection("BankAccount").document(userid)
                                        .set(user).addOnSuccessListener {
                                            Log.d(
                                                "registered",
                                                "onSuccess: user UPIID is register with $userid"
                                            )
                                        }
                                    val user2 = mutableMapOf<String, String>()
                                    user2["Amount"] = amount
                                    fstore.collection("Withdrawals").document(userid)
                                        .set(user2).addOnCompleteListener {
                                            Log.d(
                                                "registered",
                                                "onSuccess: user UPIID is register with $userid"
                                            )
                                        }
                                    Toast.makeText(this, "Details are saved", Toast.LENGTH_LONG)
                                        .show()
                                    etUPI.visibility = View.GONE
                                    btconfirmupi.visibility = View.GONE
                                    txtRequestWithdraw.visibility = View.VISIBLE
                                    Thread.sleep(5000)
                                    val withdrawintent =
                                        Intent(this@Withdraw, MainActivity::class.java)
                                    startActivity(withdrawintent)
                                    finish()
                                }
                            }
                        }
                    }
                }
            }
                .addOnFailureListener { e ->
                    Toast.makeText(this@Withdraw, e.message, Toast.LENGTH_LONG).show()
                    Log.d("Androidview", e.message!!)
                }
    }
}