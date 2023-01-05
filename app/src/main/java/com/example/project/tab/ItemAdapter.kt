package com.example.project.tab

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.project.ItemObject
import com.example.project.R
import com.example.project.databinding.ActivityMainBinding
import com.google.protobuf.Field.Cardinality


class ItemAdapter(private val itemList: ArrayList<ItemObject>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.activity_title_work,
            parent, false)
        val binding = Cardinality
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentItem = itemList[position]

        val icon: String?,
        val iconname:String?,
        val status : String?,
        val title : String?)

        holder.textView1.icon = currentItem.icon
        holder.textView2.text = currentItem.iconname
        holder.textView3.text = currentItem.status
        holder.textView4.text = currentItem.title

        // Change background color and text color based on status
        if (currentItem.text4 == "Pending") {
            holder.cardView.setCardBackgroundColor(holder.cardView.context.getColor(R.color.pending))
            holder.textView4.setTextColor(holder.cardView.context.getColor(R.color.white))
        } else {
            holder.cardView.setCardBackgroundColor(holder.cardView.context.getColor(R.color.completed))
            holder.textView4.setTextColor(holder.cardView.context.getColor(R.color.black))
        }
    }

    override fun getItemCount(): Int {itemList.size}

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cardView = itemView.cardView
        val textView1 = itemView.icon_title
        val textView2 = itemView.project_title
        val textView3 = itemView.date
        val textView4 = itemView.status
    }


}