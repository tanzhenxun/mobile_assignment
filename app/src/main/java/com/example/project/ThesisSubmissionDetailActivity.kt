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
    private var userId = FirebaseAuth.getInstance().currentUser!!.uid

    // Variable for data from previous activity
    private var submissionId: String? = null

    // Variable for view or button
    private var tvTitle: TextView? = null
    private var tvStatus: TextView? = null
    private var tvSubmissionDate: TextView? = null
    private var tvFeedback: TextView? = null
    private var btnRedo: Button? = null

    // Variable for data from "user" sub-collection with specific document id
    private var title: String? = null
    private var submissionStatus: String? = null
    private var submissionDate: String? = null
    private var feedback: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesis_submission_detail)

        // Declare variable for data from previous activity
        submissionId = intent.getStringExtra("submissionId")

        // Declare variable for view or button
        tvTitle = findViewById(R.id.project_title)
        tvStatus = findViewById(R.id.status)
        tvSubmissionDate = findViewById(R.id.submission_date)
        tvFeedback = findViewById(R.id.input_feedback)
        btnRedo = findViewById(R.id.btn_undo)

        // Query to "submission" collection with specific document id
        val submissionReference = db.collection("submission")

        // Query to get data from the "users" collection with specific document id
        submissionReference.document(submissionId.toString()).get()
            .addOnSuccessListener { submissionSnapshot ->
                if (submissionSnapshot.exists()) {
                    submissionSnapshot.reference.collection("users").document(userId).get()
                        .addOnSuccessListener { subCollectionSnapshot ->
                            if (subCollectionSnapshot.exists()) {
                                title = subCollectionSnapshot.getString("title")
                                submissionStatus =
                                    subCollectionSnapshot.getString("submission_status")
                                submissionDate = subCollectionSnapshot.getString("submission_date")
                                feedback = subCollectionSnapshot.getString("feedback")

                                tvTitle!!.text = title
                                tvStatus!!.text = submissionStatus
                                tvSubmissionDate!!.text = submissionDate
                                tvFeedback!!.text = feedback

                                if (submissionStatus != null) {
                                    checkStatus()
                                }
                            }
                        }
                }
            }
    }

    // Pending, Pending(TextView)
    private fun checkStatus() {
        when (submissionStatus) {
            "Pending" -> {
                val colorStateList = ContextCompat.getColorStateList(this, R.color.deep_yellow)
                tvStatus!!.setBackgroundTintList(colorStateList)

                // Will hide the button, but it will still take up space in the layout
                btnRedo!!.visibility = View.INVISIBLE

                // Will hide the button and it will not take up any space in the layout
                // button.visibility = View.GONE

                // Will hide the button and it will not take up any space in the layout
                // button.isVisible = false
            }
            "Approved" -> {
                val colorStateList = ContextCompat.getColorStateList(this, R.color.deep_green)
                tvStatus!!.setBackgroundTintList(colorStateList)
                // Will hide the button, but it will still take up space in the layout
                btnRedo!!.visibility = View.INVISIBLE
            }
            else -> {}
        }
    }
}