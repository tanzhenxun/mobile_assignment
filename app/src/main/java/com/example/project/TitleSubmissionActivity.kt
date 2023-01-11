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

class TitleSubmissionActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_title_submission)
        val submissionId = intent.getStringExtra("submissionId")
        val submissionStatus = intent.getStringExtra("submissionStatus")
        val Label = intent.getStringExtra("label")
        val Deadline = intent.getStringExtra("deadline")
        val Overdue = intent.getBooleanExtra("overdue", false)

        Toast.makeText(this, submissionId, Toast.LENGTH_LONG).show()

        var title = findViewById<TextView>(R.id.input_title)
        var comment = findViewById<TextView>(R.id.input_comment)
        val btn_submit = findViewById<Button>(R.id.submit_button)

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("submission").document(submissionId.toString()).get()
            .addOnSuccessListener { submissionSnapshot ->
            if(submissionSnapshot.exists()){
                Toast.makeText(this, "Second Success", Toast.LENGTH_LONG).show()
                submissionSnapshot.reference.collection("users").document(userId).get()
                    .addOnSuccessListener { userSnapshot ->
                        if(userSnapshot.exists()){
                            val projectTitle = userSnapshot.getString("title")
                            val projectAbstract = userSnapshot.getString("abstract")
                            Toast.makeText(this, "Last Success", Toast.LENGTH_LONG).show()
                            title.text = projectTitle
                            comment.text = projectAbstract
                        }
                    }
            }

        btn_submit.setOnClickListener {
            val Title = title.text.toString().trim()
            val Comment = comment.text.toString().trim()

            // Current Date Time format
            val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

            // Current Date Time
            val currentDateTime = Calendar.getInstance().time

            // Convert current date time from (Date to String)
            val dateSubmit = dateFormat.format(currentDateTime)

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
                        "title" to Title,
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

            if(submissionStatus == "Pending"){
                finish()
            } else{
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
}
