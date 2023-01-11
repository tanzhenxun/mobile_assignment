package com.example.project

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.text.SimpleDateFormat
import java.util.*

class ProposalSubmissionActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var mDB: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance()
    private var fileUrl : Uri? = null
    private var Deadline : String? = null
    private var Label : String? = null
    private var submissionId : String? = null

    private var Overdue : Boolean? = null

    private var fileName : TextView? = null
    private var comment : TextView? = null


    private var MY_CODE_REQUEST: Int = 100;

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(MY_CODE_REQUEST, result)
    }

    @SuppressLint("SimpleDateFormat", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proposal_submission)
        submissionId = intent.getStringExtra("submissionId")
        Label = intent.getStringExtra("label")
        Deadline = intent.getStringExtra("deadline")
        Overdue = intent.getBooleanExtra("overdue", false)
        Toast.makeText(this, submissionId, Toast.LENGTH_LONG).show()

        val upload = findViewById<TextView>(R.id.upload_proposal)
        comment = findViewById<EditText>(R.id.input_comment)
        val btn_upload = findViewById<Button>(R.id.upload_button)
        val btn_submit = findViewById<Button>(R.id.submit_button)
        fileName = findViewById(R.id.fileName)
        upload.text = Label

        btn_upload.setOnClickListener {
            selectImage();
        }

        btn_submit.setOnClickListener {
            Log.d("Image Name", fileUrl.toString())
            uploadImage();
        }
    }
    @SuppressLint("SimpleDateFormat")
    private fun uploadImage(){
        var fileRef : StorageReference? = storageReference.reference

        var documentRef = fileRef!!.child("uploadedFile/${fileName!!.text}")

        // It will upload the file based on the url that we uploaded
        documentRef.putFile(fileUrl!!)
            .addOnSuccessListener {
            val Comment = comment!!.text.toString().trim()

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
                        "user_id" to userId,
                        "file_Submited" to fileName!!.text
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

            val intent = Intent(this, ProposalSubmissionDetailActivity::class.java)
            intent.putExtra("submissionId", newDocument.id)
            // Remove current activity history to prevent navigate back
            //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
            startActivity(intent)

            // Back to the previous Activity.
            finish()
        }
    }
    private fun selectImage(){
        var intent = Intent();

        // if you want find image => "image/* , word or pdf => "application/*
        intent.setType("application/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startForResult.launch(intent)
    }

    private fun onActivityResult(requestCode: Int, result: ActivityResult) {
        if (result.resultCode == Activity.RESULT_OK) {
            val intent = result.data
            when (requestCode) {
                MY_CODE_REQUEST -> {
                    fileUrl = intent!!.data
                    fileName!!.text = getFileNameFromUri(this, fileUrl!!)
                }
            }
        }
    }

    @SuppressLint("Range")
    private fun getFileNameFromUri(context: Context, uri: Uri): String? {
        val fileName: String?
        val cursor = context.contentResolver.query(uri, null, null, null, null)
        cursor?.moveToFirst()
        fileName = cursor?.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
        cursor?.close()
        return fileName
    }

}