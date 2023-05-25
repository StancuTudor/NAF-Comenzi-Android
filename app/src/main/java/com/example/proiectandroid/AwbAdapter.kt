package com.example.proiectandroid
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AwbAdapter(var mList: List<AwbData>) :
    RecyclerView.Adapter<AwbAdapter.AwbViewHolder>() {

    inner class AwbViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val logo : ImageView = itemView.findViewById(R.id.logoIv)
        val titleTv : TextView = itemView.findViewById(R.id.titleTv)
    }

    fun setFilteredList(mList: List<AwbData>){
        this.mList = mList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AwbViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.each_item , parent , false)
        return AwbViewHolder(view)
    }

    override fun onBindViewHolder(holder: AwbViewHolder, position: Int) {
        holder.logo.setImageResource(R.drawable.naf_logo)
        holder.titleTv.text = mList[position].awb.toString()
    }

    override fun getItemCount(): Int {
        return mList.size
    }
}