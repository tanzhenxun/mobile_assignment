package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class TitleSubmissionActivity : AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser!!.uid

    // Variable for data from previous activity
    private var submissionId: String? = null
    private var submissionStatus: String? = null
    private var Label: String? = null
    private var Title: String? = null
    private var Abstract: String? = null
    private var Deadline: String? = null
    private var Overdue: Boolean? = null

    // Variable for view or button
    private var title: TextView? = null
    private var comment: TextView? = null
    private var btn_submit: Button? = null

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_submission)

        // Declare variable for data from previous activity
        submissionId = intent.getStringExtra("submissionId")
        submissionStatus = intent.getStringExtra("submissionStatus")
        Label = intent.getStringExtra("label")
        Deadline = intent.getStringExtra("deadline")
        Overdue = intent.getBooleanExtra("overdue", false)

        // Declare variable for view or button
        title = findViewById(R.id.input_title)
        comment = findViewById(R.id.input_comment)
        btn_submit = findViewById(R.id.submit_button)

        // Query to get the submission information
        db.collection("submission").document(submissionId.toString()).get()
            .addOnSuccessListener { submissionSnapshot ->
                if (submissionSnapshot.exists()) {
                    submissionSnapshot.reference.collection("users").document(userId).get()
                        .addOnSuccessListener { userSnapshot ->
                            if (userSnapshot.exists()) {
                                val projectTitle = userSnapshot.getString("title")
                                val projectAbstract = userSnapshot.getString("abstract")
                                title!!.text = projectTitle
                                comment!!.text = projectAbstract
                            }
                        }
                }
            }

        // When press submit button
        btn_submit!!.setOnClickListener {
            saveData()
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun saveData() {
        val Title = title!!.text.toString().trim()
        val Comment = comment!!.text.toString().trim()

        // Current Date Time format
        val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

        // Current Date Time
        val currentDateTime = Calendar.getInstance().time

        // Convert current date time from (Date to String)
        val dateSubmit = dateFormat.format(currentDateTime)

        // Query to get user information
        db.collection("users").document(userId).get()
            .addOnSuccessListener { usersSnapshot ->
                val lastName = usersSnapshot.getString("last_name")
                val firstName = usersSnapshot.getString("first_name")
                val studentId = usersSnapshot.getString("std_id")

                // Get to same document id of "submission" collection
                val newDocument = db.collection("submission").document(submissionId.toString())

                val data = mapOf(
                    "last_name" to lastName,
                    "first_name" to firstName,
                    "std_id" to studentId,
                    "label" to Label,
                    "deadline" to Deadline,
                    "submission_id" to newDocument.id,
                    "title" to Title,
                    "abstract" to Comment,
                    "submission_date" to dateSubmit,
                    "submission_status" to "Pending",
                    "overdue" to Overdue,
                    "user_id" to userId
                )

                // Query to save the data into "users" sub-collection of "submission" collection
                newDocument.collection("users").document(userId).set(data)
                    .addOnSuccessListener { documentReference ->
                        // The data was successfully saved
                        Toast.makeText(
                            baseContext, "Submission has been submitted successfully",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                    .addOnFailureListener {
                        // The save failed
                        Toast.makeText(
                            baseContext,
                            "Submission was not able to submit, please try again",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                // Intent activity based on submission status
                if (submissionStatus == "Pending") {
                    finish()
                } else {
                    val intent = Intent(this, TitleSubmissionDetailActivity::class.java)
                    intent.putExtra("submissionId", newDocument.id)
                    // Remove current activity history to prevent navigate back
                    //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
                    startActivity(intent)

                    // Back to the previous Activity.
                    finish()
                }
            }
    }
}