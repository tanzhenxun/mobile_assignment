package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.project.student.PosterSubmissionActivity
import com.example.project.student.PosterSubmissionDetailActivity
import com.example.project.student.Submission
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PosterAdapter(private val submissionList: ArrayList<Submission>) :
    RecyclerView.Adapter<PosterAdapter.MyViewHolder>() {

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val label: TextView = itemView.findViewById(R.id.icon_title)
        val due_date: TextView = itemView.findViewById(R.id.subDate)
        val Status: TextView = itemView.findViewById(R.id.status)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val Title: TextView = itemView.findViewById(R.id.project_title)
        val colorStateListYellow = ContextCompat.getColorStateList(itemView.context, R.color.deep_yellow)
        val colorStateListRed = ContextCompat.getColorStateList(itemView.context, R.color.deep_red)
        val colorStateListGreen = ContextCompat.getColorStateList(itemView.context, R.color.deep_green)

        val logo: ImageView = itemView.findViewById(R.id.icon)
        val titleDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.title_icon)
        val posterDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.poster_icon)
        val thesisReportDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.thesis_report_icon)
        val proposalReportDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.proposal_report_icon)
        val proposalPPTDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.vector)
        val thesisPPTDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.vector)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.item_layout, parent, false)
        return MyViewHolder(itemView)
    }

    @SuppressLint("SimpleDateFormat", "SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.label.text = submissionList[position].label
        holder.due_date.text = submissionList[position].deadline
        holder.Status.text = submissionList[position].submission_status
        holder.Title.text = submissionList[position].title

        // Replacing the submission_status

        val submissionId = submissionList[position].submission_id
        val Label = submissionList[position].label

        // Deadline Date
        val dlDate = SimpleDateFormat("dd-MM-yyyy HH:mm").parse(holder.due_date.text as String)
        val dlHourFormat = SimpleDateFormat("HH")
        val dlMinuteFormat = SimpleDateFormat("mm")

        val dlHour = Integer.parseInt(dlHourFormat.format(dlDate as Date))
        val dlMinute = Integer.parseInt(dlMinuteFormat.format(dlDate))

        val calendar1 = Calendar.getInstance()
        calendar1.set(Calendar.HOUR_OF_DAY, dlHour)
        calendar1.set(Calendar.MINUTE, dlMinute)

        when(Label){
            "Title" -> {
//                holder.logo.setImageDrawable(null)
//                holder.logo.setBackgroundResource(R.drawable.title)
                holder.logo.setImageDrawable(holder.titleDrawable)
            }
            "Poster" -> {
                holder.logo.setImageDrawable(holder.posterDrawable)
            }
            "Proposal Report" -> {
                holder.logo.setImageDrawable(holder.proposalReportDrawable)
            }
            "Proposal PPT" -> {
                holder.logo.setImageDrawable(holder.proposalPPTDrawable)
            }
            "Thesis Report" -> {
                holder.logo.setImageDrawable(holder.thesisReportDrawable)
            }
            "Thesis PPT" -> {
                holder.logo.setImageDrawable(holder.thesisPPTDrawable)
            }
            else->{}
        }

        // Replace student mark
//        val db = FirebaseFirestore.getInstance()
//        val userId = FirebaseAuth.getInstance().currentUser!!.uid
//
//        db.collection("submission").document(submissionId.toString()).get()
//            .addOnSuccessListener { submissionSnapshot ->
//                submissionSnapshot.reference.collection("users").document(userId).get()
//                    .addOnSuccessListener { usersSnapshot ->
//                        usersSnapshot.reference.collection("mark").get()
//                            .addOnSuccessListener { markSnapshot ->
//                                for(data in markSnapshot){
//                                    val mark = data.getString("proposal")
//                                    holder.label.text = mark
//                                }
//                            }
//                    }
//            }

        // Current Date
        val currentDate = Calendar.getInstance().time

        val calendar2 = Calendar.getInstance()
        val currentTime = Date()
        calendar2.setTime(currentTime)

        var overdue = false

        holder.cardView.setOnClickListener { view ->
//            Toast.makeText(view.context, submissionId, Toast.LENGTH_LONG)
//                .show()

            // If Pending, Rejected or Approve, then show detail.
            fun checkDetail(){
                val intent1 = Intent(view.context, PosterSubmissionDetailActivity::class.java)
                intent1.putExtra("submissionId", submissionId)
                view.context.startActivity(intent1)
            }

            // Overdue, Pending, Rejected, Approve / Graded
            when (holder.Status.text) {
                "" -> {
                    val intent1 = Intent(view.context, PosterSubmissionActivity::class.java)
                    intent1.putExtra("submissionId", submissionId)
                    intent1.putExtra("label", holder.label.text)
                    intent1.putExtra("deadline", holder.due_date.text)
                    intent1.putExtra("overdue", overdue)

                    view.context.startActivity(intent1)
                }
                "Overdue" -> {
                    val intent1 = Intent(view.context, PosterSubmissionActivity::class.java)
                    intent1.putExtra("submissionId", submissionId)
                    intent1.putExtra("label", holder.label.text)
                    intent1.putExtra("deadline", holder.due_date.text)
                    intent1.putExtra("overdue", overdue)

                    view.context.startActivity(intent1)
                }
                "Pending" -> {
                    checkDetail()
                }
                "Rejected" -> {
                    checkDetail()
                }
                "Approved" -> {
                    checkDetail()
                }
            }
        }

        fun checkDate() {
            //Check if the current date is after the deadline
            if (currentDate.after(dlDate)) {
                holder.Status.visibility = View.VISIBLE
                holder.Status.backgroundTintList = holder.colorStateListRed
//                holder.Status.setBackgroundResource(R.color.deep_red)
                holder.cardView.setBackgroundResource(R.color.red);
                holder.Status.text = "Overdue"
                overdue = true

            } else if (currentDate == dlDate && calendar2.after(calendar1)) {
                holder.Status.visibility = View.VISIBLE
                holder.Status.backgroundTintList = holder.colorStateListRed
//                holder.Status.setBackgroundResource(R.color.deep_red)
                holder.cardView.setBackgroundResource(R.color.red);
                holder.Status.text = "Overdue"
                overdue = true
            } else{
                overdue = false
            }
        }

        // If project_title is blank then Textview will gone
        if(holder.Title.text == "" ) {
            holder.Title.visibility = View.GONE
        }

        // Check status
        when (holder.Status.text) {
            "" -> {
                holder.Status.visibility = View.INVISIBLE
                holder.cardView.setBackgroundResource(R.color.grey);
//                holder.Status.setBackgroundColor(Color.TRANSPARENT)
                //holder.Status.setBackgroundColor(Color.parseColor("#e7eecc"))
                checkDate()
            }
            "Pending" -> {
                holder.Status.backgroundTintList = holder.colorStateListYellow
//                holder.Status.setBackgroundResource(R.color.deep_yellow)
                holder.cardView.setBackgroundResource(R.color.yellow);
            }
            "Approved" -> {
                holder.Status.backgroundTintList = holder.colorStateListGreen
                holder.cardView.setBackgroundResource(R.color.green);
            }
            else -> {}
        }
    }

    override fun getItemCount(): Int {
        return submissionList.size
    }
}