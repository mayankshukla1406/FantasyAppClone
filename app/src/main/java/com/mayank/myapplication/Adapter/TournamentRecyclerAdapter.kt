package com.mayank.myapplication.Adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mayank.myapplication.Activity.DescriptionActivity
import com.mayank.myapplication.Model.TournamentList
import com.mayank.myapplication.R
import org.w3c.dom.Text

class TournamentRecyclerAdapter(val context: Context?,val itemList : ArrayList<TournamentList> ) :
    RecyclerView.Adapter<TournamentRecyclerAdapter.TournamentViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TournamentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.recylcer_tournaments_single_row,parent,false)
        return TournamentViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }
    override fun onBindViewHolder(holder: TournamentViewHolder, position: Int) {
        val list = itemList[position]
        holder.txtTournamentName.text       = list.tournamentName
        holder.txtTournamentTime.text       = list.tournamentTime
        holder.txtTournamentAmount.text     = list.tournamentAmount
        holder.txttournamentPrizePool.text  = list.tournamentPrizePool
        holder.txtdescription.text          = "Show your Skils in  "+list.tournamentMap+" Map and Win Cash Prizes"
        holder.imgTournament.setImageResource(list.tournamentImage)
        holder.txtTournamentAmount.setOnClickListener {
            val intent = Intent(context, DescriptionActivity::class.java)
            if (intent.getStringExtra("email") != null){
                intent.putExtra("tournamentID", list.tournamentID)
            intent.putExtra("emailid", intent.getStringExtra("email"))
            context?.startActivity(intent)
        }
            else
            {
                intent.putExtra("tournamentID", list.tournamentID)
                context?.startActivity(intent)
            }
        }
    }
    class TournamentViewHolder(view:View): RecyclerView.ViewHolder(view)
    {
        val txtTournamentName      : TextView     = view.findViewById(R.id.txtTournamentName)
        val txtTournamentTime      : TextView     = view.findViewById(R.id.txtTournamentTime)
        val txtTournamentAmount    : Button       = view.findViewById(R.id.txtTournamentAmount)
        val txttournamentPrizePool : TextView     = view.findViewById(R.id.txttournamentPrizePool)
        val txtdescription         : TextView     = view.findViewById(R.id.txtdescription)
        val imgTournament          : ImageView    = view.findViewById(R.id.imgTournament)
        val content                : LinearLayout = view.findViewById(R.id.Content)
    }
}