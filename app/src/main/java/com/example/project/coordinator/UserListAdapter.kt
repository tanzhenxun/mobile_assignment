package com.example.project.coordinator

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.google.firebase.firestore.FirebaseFirestore

class UserListAdapter(private var viewGradeList: ArrayList<Mark>) :
    RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val studentName: TextView = itemView.findViewById(R.id.student_name3)
        var scoreBar: TextView = itemView.findViewById(R.id.scorebar)
        val colorStateListRed = ContextCompat.getColorStateList(itemView.context, R.color.deep_red)
        val colorStateListYellow = ContextCompat.getColorStateList(itemView.context, R.color.deep_yellow)
        val colorStateListGreen = ContextCompat.getColorStateList(itemView.context, R.color.deep_green)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.grade_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val db = FirebaseFirestore.getInstance()
        val userId = viewGradeList[position].user_id
        val submissionId = viewGradeList[position].submission_id

        db.collection("mark").document(userId!!).get().addOnSuccessListener { markSnapshot ->
            if (markSnapshot.exists()) {
                val totalMark = markSnapshot.getLong("total_mark").toString()
                val totalMarkInt = totalMark.toInt()
                holder.scoreBar.text = totalMark

                if(totalMarkInt in 1..19){
                    holder.scoreBar.backgroundTintList = holder.colorStateListRed
                }else if (totalMarkInt in 20..39){
                    holder.scoreBar.backgroundTintList = holder.colorStateListYellow
                }else{
                    holder.scoreBar.backgroundTintList = holder.colorStateListGreen
                }
            }
        }

        holder.cardView.setOnClickListener{ view ->
            val intent = Intent(view.context, MarkEndrosementActivity::class.java)
            intent.putExtra("UserId", userId)
            intent.putExtra("submissionId", submissionId)
            intent.action = "com.example.package.GradeFragment"
            view.context.startActivity(intent)
        }

        db.collection("users").document(userId.toString()).get()
            .addOnSuccessListener { userSnapshot ->
                if (userSnapshot.exists()) {
                    var firstName: String? = null
                    var lastName: String? = null

                    firstName = userSnapshot.getString("first_name")
                    lastName = userSnapshot.getString("last_name")
                    holder.studentName.text = "$firstName $lastName"
                }
            }
    }

    override fun getItemCount(): Int {
        return viewGradeList.size
    }
}