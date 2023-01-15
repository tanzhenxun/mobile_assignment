package com.example.project.supervisor.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.content.res.AppCompatResources
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.project.*
import com.example.project.supervisor.SubmissionApprovalActivity
import com.example.project.supervisor.SubmissionApprovalDetailActivity
import com.example.project.supervisor.SubmissionRatingActivity
import com.example.project.supervisor.ViewSubmission

class ViewSubmissionAdapter(private var viewSubmissionList:ArrayList<ViewSubmission>): RecyclerView.Adapter<ViewSubmissionAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val studentName: TextView = itemView.findViewById(R.id.student_name)
        val label: TextView = itemView.findViewById(R.id.icon_title)
        val cardView: CardView = itemView.findViewById(R.id.cardView)
        val logo: ImageView = itemView.findViewById(R.id.icon)
        val titleDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.title_icon)
        val posterDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.poster_icon)
        val thesisReportDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.thesis_report_icon)
        val proposalReportDrawable = AppCompatResources.getDrawable(itemView.context, R.drawable.proposal_report_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.submission_item_list, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
//        if (viewSubmissionList.isNotEmpty() && position in viewSubmissionList.indices) {
//            holder.studentName.text = viewSubmissionList[position].first_name + " " + viewSubmissionList[position].last_name
//        }
        holder.studentName.text = viewSubmissionList[position].first_name + " " + viewSubmissionList[position].last_name
        holder.label.text = viewSubmissionList[position].label

        // Declare variable to store user submission data and pass to submission detail
        val submissionId = viewSubmissionList[position].submission_id
        val UserId = viewSubmissionList[position].user_id

        val Label = viewSubmissionList[position].label
        val studentName = holder.studentName.text
        val studentId = viewSubmissionList[position].std_id
        val title = viewSubmissionList[position].title
        val submissionStatus = viewSubmissionList[position].submission_status
        val abstract = viewSubmissionList[position].abstract
        val subDate = viewSubmissionList[position].submission_date
        val fileSub = viewSubmissionList[position].file_Submited
        // Show different colour based on status
        when(submissionStatus){
            "Pending" -> {}
            "Approved" -> {
                holder.cardView.setBackgroundResource(R.color.lightgreen)
            }
            "Rejected" -> {
                holder.cardView.setBackgroundResource(R.color.lightred)
//                Toast.makeText(holder.itemView.context, submissionStatus, Toast.LENGTH_LONG).show()
            }
        }


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
            "Thesis Report" -> {
                holder.logo.setImageDrawable(holder.thesisReportDrawable)
            }
            else->{}
        }

        // When press the card will redirect to another activity based on label
        holder.cardView.setOnClickListener { view ->
//            Toast.makeText(view.context, submissionId, Toast.LENGTH_LONG).show()

            // Store necessary data for next activity
            fun storeData(intent:Intent){
                intent.putExtra("Label", Label)
                intent.putExtra("studentName", studentName)
                intent.putExtra("studentId", studentId)
                intent.putExtra("title", title)
                intent.putExtra("submissionStatus", submissionStatus)
                intent.putExtra("subDate", subDate)
                intent.putExtra("abstract", abstract)
                intent.putExtra("submissionId", submissionId)
                intent.putExtra("fileSub", fileSub)
                intent.putExtra("UserId", UserId)
            }

            // If submission status is Pending
            if (submissionStatus == "Pending"){
                // Check label and intent to different activities
                // Title, Poster, Proposal PPT, Thesis PPT
                if(holder.label.text == "Title" || holder.label.text == "Poster" ||
                    holder.label.text == "Proposal PPT" || holder.label.text == "Thesis PPT"){

                    val intent = Intent(view.context, SubmissionApprovalActivity::class.java)
                    storeData(intent)
                    view.context.startActivity(intent)
                }
                // Proposal report, Thesis report
                else{
                    val intent = Intent(view.context, SubmissionRatingActivity::class.java)
                    storeData(intent)
                    view.context.startActivity(intent)
                }
            }
            // If submission status is Approved or Rejected
            else{
                // Check label and intent to different activities
                // Title, Poster, Proposal PPT, Thesis PPT
                if(holder.label.text == "Title" || holder.label.text == "Poster" ||
                    holder.label.text == "Proposal PPT" || holder.label.text == "Thesis PPT"){
                    val intent = Intent(view.context, SubmissionApprovalDetailActivity::class.java)
                    storeData(intent)
                    view.context.startActivity(intent)
                }
                // Proposal report, Thesis report
                else{}
            }
        }


    }

    override fun getItemCount(): Int {
        return viewSubmissionList.size
    }
}