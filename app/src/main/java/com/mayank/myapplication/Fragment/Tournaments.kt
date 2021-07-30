package com.mayank.myapplication.Fragment

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.RelativeLayout
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.mayank.myapplication.Adapter.TournamentRecyclerAdapter
import com.mayank.myapplication.Model.TournamentList
import com.mayank.myapplication.R
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_tournaments.*


open class Tournaments : Fragment() {
    lateinit var RecyclerDashboard    : RecyclerView
    lateinit var LayoutManager        : RecyclerView.LayoutManager
    lateinit var db                   : DocumentReference
    lateinit var swipe                : SwipeRefreshLayout
    lateinit var progressLayout       : RelativeLayout
    lateinit var progressBar          : ProgressBar
    lateinit var fstore               : FirebaseFirestore
    var tournamentInfoList = arrayListOf<TournamentList>()
    lateinit var RecyclerAdapter: TournamentRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_tournaments, container, false)
        fstore              = FirebaseFirestore.getInstance()
        RecyclerDashboard   = view.findViewById(R.id.recyclerTournaments)
        LayoutManager       = LinearLayoutManager(activity)
        progressLayout      = view.findViewById(R.id.progressLayout)
        progressBar         = view.findViewById(R.id.progressBar)
        progressLayout.visibility = View.VISIBLE
                        for(i in 1..5) {
                        db = fstore.collection("tournamentlists")
                            .document(i.toString())
                                    db.addSnapshotListener { value, error ->
                                        progressLayout.visibility = View.GONE
                                        if (error != null) {
                                            Log.w("YourTag", "Listen failed.", error)
                                        }
                                        val obj = TournamentList(
                                            i.toString(),
                                            value?.getString("tournamentName").toString(),
                                            value?.getString("tournamentDate").toString(),
                                            value?.getString("tournamentAmount").toString(),
                                            value?.getString("tournamentPrizePool").toString(),
                                            value?.getString("tournamentMap").toString(),
                                            R.drawable.pubgback
                                        )
                                        tournamentInfoList.add(obj)
                                        RecyclerAdapter = TournamentRecyclerAdapter(
                                            activity as? Context,
                                            tournamentInfoList
                                        )
                                        RecyclerDashboard.adapter = RecyclerAdapter
                                        RecyclerDashboard.layoutManager = LayoutManager
                                        RecyclerDashboard.addItemDecoration(
                                            DividerItemDecoration(
                                                RecyclerDashboard.context,
                                                (LayoutManager as LinearLayoutManager).orientation
                                            )
                                        )
                                    }
                                }
        swipe = view.findViewById(R.id.swiperefresh)
        swipe.setOnRefreshListener(object : SwipeRefreshLayout.OnRefreshListener
        {
            override fun onRefresh() {
                RecyclerAdapter.notifyDataSetChanged()
                swipe.isRefreshing=false
            }
        })
        return view
    }
}
