package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

class ThesisSubmissionActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesis_submission)
        val submissionId = intent.getStringExtra("submissionId")
        val Label = intent.getStringExtra("label")
        val Deadline = intent.getStringExtra("deadline")
        val Overdue = intent.getBooleanExtra("overdue", false)

        Toast.makeText(this, submissionId, Toast.LENGTH_LONG).show()

        val upload = findViewById<TextView>(R.id.upload_thesis)
        val comment = findViewById<EditText>(R.id.input_comment)
        val btn_submit = findViewById<Button>(R.id.submit_button)

        upload.text = Label

        btn_submit.setOnClickListener {
            val Comment = comment.text.toString().trim()

            // Current Date Time format
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

            // Current Date Time
            val currentDateTime = Calendar.getInstance().time

            // Convert current date time from (Date to String)
            val dateSubmit = dateFormat.format(currentDateTime)

            val userId = FirebaseAuth.getInstance().currentUser!!.uid

            // Get to same submitted submission id of "submission" collection
            val newDocument = db.collection("submission").document(submissionId.toString())

            db.collection("users").document(userId).get()
                .addOnSuccessListener { usersSnapshot ->
                    val lastName = usersSnapshot.getString("last_name")
                    val firstName = usersSnapshot.getString("first_name")
                    val studentId = usersSnapshot.getString("std_id")

                    val data = mapOf(
                        "last_name" to lastName,
                        "first_name" to firstName,
                        "std_id" to studentId,
                        "label" to Label,
                        "deadline" to Deadline,
                        "submission_id" to newDocument.id,
                        "abstract" to Comment,
                        "submission_date" to dateSubmit,
                        "submission_status" to "Pending",
                        "overdue" to Overdue,
                        "user_id" to userId
                    )

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
                                baseContext, "Submission was not able to submit, please try again",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                }

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