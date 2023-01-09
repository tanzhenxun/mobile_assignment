package com.example.project

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ThesisSubmissionDetailActivity : AppCompatActivity() {
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesis_submission_detail)
        val submissionId = intent.getStringExtra("submissionId")

        val Title = findViewById<TextView>(R.id.project_title)
        val Status = findViewById<TextView>(R.id.status)
        val Submissiondate = findViewById<TextView>(R.id.submission_date)
        val Feedback = findViewById<TextView>(R.id.input_feedback)
        val btnUndo = findViewById<Button>(R.id.btn_undo)

        val submissionReference = db.collection("submission")
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        submissionReference.document(submissionId.toString()).get()
            .addOnSuccessListener { submissionSnapshot ->
                if(submissionSnapshot.exists()){
                    submissionSnapshot.reference.collection("users").document(userId).get()
                        .addOnSuccessListener { subCollectionSnapshot ->
                            if (subCollectionSnapshot.exists()){
                                val title = subCollectionSnapshot.getString("title")
                                val submissionStatus = subCollectionSnapshot.getString("submission_status")
                                val submissionDate = subCollectionSnapshot.getString("submission_date")
                                val feedback = subCollectionSnapshot.getString("feedback")

                                Title.text = title
                                Status.text = submissionStatus
                                Submissiondate.text = submissionDate
                                Feedback.text = feedback

                                if (submissionStatus != null) {
                                    checkStatus(submissionStatus, Status, btnUndo)
                                }
                            }
                        }
                }
            }
    }
    // Pending, Pending(TextView)
    fun checkStatus(status: String, Status: TextView, btnUndo: Button){
        when(status){
            "Pending" -> {
                val colorStateList = ContextCompat.getColorStateList(this, R.color.deep_yellow)
                Status.setBackgroundTintList(colorStateList)

                // Will hide the button, but it will still take up space in the layout
                btnUndo.visibility = View.INVISIBLE

                // Will hide the button and it will not take up any space in the layout
                // button.visibility = View.GONE

                // Will hide the button and it will not take up any space in the layout
                // button.isVisible = false
            }
            "Approved" -> {
                val colorStateList = ContextCompat.getColorStateList(this, R.color.deep_green)
                Status.setBackgroundTintList(colorStateList)
                // Will hide the button, but it will still take up space in the layout
                btnUndo.visibility = View.INVISIBLE
            }
            else -> {}
        }
    }
}