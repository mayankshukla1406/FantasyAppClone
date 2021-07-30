package com.mayank.myapplication.mayank

import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.WindowManager
import android.widget.FrameLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.iid.FirebaseInstanceId
import com.mayank.myapplication.Fragment.*
import com.mayank.myapplication.R

class MainActivity : AppCompatActivity() {
    lateinit var drawerLayout: DrawerLayout
    var tag = "TAG"

    lateinit var cordinatorLayout    : CoordinatorLayout
    lateinit var frameLayout         : FrameLayout
    lateinit var toolbar             : Toolbar
    lateinit var auth                : FirebaseAuth
    lateinit var navigationView      : NavigationView
    lateinit var update              : String
    lateinit var fstore              : FirebaseFirestore
    lateinit var db                  : DocumentReference
    lateinit var updatelink          : String
    lateinit var version             : String
    lateinit var version1            : String
    var previousMenuItem: MenuItem? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        drawerLayout      = findViewById(R.id.drawerLayout)
        cordinatorLayout  = findViewById(R.id.coordinatorLayout)
        frameLayout       = findViewById(R.id.frameLayout)
        toolbar           = findViewById(R.id.toolbar)
        navigationView    = findViewById(R.id.navigationView)
        auth              = FirebaseAuth.getInstance()
        fstore            = FirebaseFirestore.getInstance()
        try {
            val pInfo: PackageInfo = this.getPackageManager().getPackageInfo(packageName, 0)
            version  = pInfo.versionName
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        db                = fstore.collection("update").document("available")
        db.addSnapshotListener { value, error ->
            if (error != null) {
                Log.w("YourTag", "Listen failed.", error)
            }
            update        = value?.getString("answer").toString()
            updatelink    = value?.getString("link").toString()
            version1      = value?.getString("version").toString()
            if (update == "yes"&&version!=version1) {
                val builder = AlertDialog.Builder(this)
                builder.setMessage("Update available! Click update to download new Version")
                builder.setPositiveButton("Update") { dialog, which ->
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(updatelink))
                    startActivity(intent)
                }
                val alertDialog : AlertDialog = builder.create()
                alertDialog.show()
            }
        }
        setUpToolbar()
        FirebaseInstanceId.getInstance().instanceId.addOnSuccessListener { instanceIdResult ->
            val token = instanceIdResult.token
            println(token)
        }
        openTournaments()
        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this@MainActivity,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )
        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()
        navigationView.setNavigationItemSelectedListener {

            if (previousMenuItem != null) {
                previousMenuItem?.isChecked = false
            }

            it.isCheckable = true
            it.isChecked = true
            previousMenuItem = it

            when (it.itemId) {
                R.id.tournaments -> {
                    openTournaments()
                    drawerLayout.closeDrawers()

                }
                R.id.profile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            Profile()
                        )
                        .commit()

                    supportActionBar?.title = "Profile"
                    drawerLayout.closeDrawers()
                }
                R.id.about -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            About()
                        )
                        .commit()

                    supportActionBar?.title = "About"
                    drawerLayout.closeDrawers()
                }
                R.id.ask -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            Ask()
                        )
                        .commit()
                    supportActionBar?.title = "Ask"
                    drawerLayout.closeDrawers()
                }
                R.id.wallet -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frameLayout,
                            Wallet()
                        )
                        .commit()

                    supportActionBar?.title = "Wallet"
                    drawerLayout.closeDrawers()
                }
                R.id.logout -> {
                    onLogout()

                }
            }
            return@setNavigationItemSelectedListener true
        }
    }

    fun setUpToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Toolbar Title"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }

        return super.onOptionsItemSelected(item)
    }

    fun openTournaments() {
        val fragment = Tournaments()
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frameLayout, fragment)
        transaction.commit()
        supportActionBar?.title = "Tournaments"
        navigationView.setCheckedItem(R.id.tournaments)
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frameLayout)

        when (frag) {
            !is Tournaments -> openTournaments()

            else -> super.onBackPressed()
        }
    }

    private fun onLogout() {
        try {
            auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            Toast.makeText(this@MainActivity, "User is Logged Out", Toast.LENGTH_LONG).show()
            startActivity(intent)
            finish()
        } catch (e: Exception) {
            Log.d(tag, "Logged out exception" + e.message, e)
        }
    }
}