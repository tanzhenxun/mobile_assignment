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
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.protobuf.DescriptorProtos.FieldDescriptorProto.Label
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*

class ThesisSubmissionActivity : AppCompatActivity() {
    private val db = FirebaseFirestore.getInstance()
    private var storageReference = FirebaseStorage.getInstance()
    private var fileUrl : Uri? = null

    // Variable for data from previous activity
    private var Deadline : String? = null
    private var Label : String? = null
    private var submissionId : String? = null
    private var Overdue : Boolean? = null

    // Variable for view or button
    private var tvLabel : TextView? = null
    private var tvComment : EditText? = null
    private var tvFileName : TextView? = null
    private var btnUpload : Button? = null
    private var btnSubmit : Button? = null

    // Declare variable for the document id of "submission" collection
    private var newDocument: DocumentReference? = null

    private var MY_CODE_REQUEST: Int = 100;

    private val startForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        onActivityResult(MY_CODE_REQUEST, result)
    }

    @SuppressLint("SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_thesis_submission)

        // Declare variable for data from previous activity
        submissionId = intent.getStringExtra("submissionId")
        Label = intent.getStringExtra("label")
        Deadline = intent.getStringExtra("deadline")
        Overdue = intent.getBooleanExtra("overdue", false)

        // Declare variable for view or button
        tvLabel = findViewById(R.id.upload_thesis)
        tvComment = findViewById(R.id.input_comment)
        tvFileName = findViewById(R.id.fileName)
        btnUpload = findViewById(R.id.upload_button)
        btnSubmit = findViewById(R.id.submit_button)

        // Show label
        tvLabel!!.text = Label

        // When upload
        btnUpload!!.setOnClickListener {
            selectImage();
        }

        // When submit
        btnSubmit!!.setOnClickListener {
            uploadImage();
        }
    }

    @SuppressLint("SimpleDateFormat")
    private fun uploadImage(){
        val fileRef : StorageReference = storageReference.reference

        // it will get the file name only, if got fileUrl.lastPathSegment
        // var documentRef = fileRef!!.child("uploadedFile/${fileUrl!!.lastPathSegment}")
        val documentRef = fileRef.child("uploadedFile/${tvFileName!!.text}")

        // It will upload the file based on the url that we uploaded
        documentRef.putFile(fileUrl!!)
            .addOnSuccessListener {
                Log.d("Image URL", fileUrl .toString())

                val Comment = tvComment!!.text.toString().trim()

                // Current Date Time format
                val dateFormat = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")

                // Current Date Time
                val currentDateTime = Calendar.getInstance().time

                // Convert current date time from (Date to String)
                val dateSubmit = dateFormat.format(currentDateTime)

                val userId = FirebaseAuth.getInstance().currentUser!!.uid

                // Query to get user data from "users" collection
                db.collection("users").document(userId).get()
                    .addOnSuccessListener { usersSnapshot ->
                        val lastName = usersSnapshot.getString("last_name")
                        val firstName = usersSnapshot.getString("first_name")
                        val studentId = usersSnapshot.getString("std_id")

                        // Query to "submission" collection with specific document id
                        newDocument = db.collection("submission").document(submissionId.toString())

                        // Prepare the data to store
                        val data = mapOf(
                            "last_name" to lastName,
                            "first_name" to firstName,
                            "std_id" to studentId,
                            "label" to Label,
                            "deadline" to Deadline,
                            "submission_id" to newDocument!!.id,
                            "abstract" to Comment,
                            "submission_date" to dateSubmit,
                            "submission_status" to "Pending",
                            "overdue" to Overdue,
                            "user_id" to userId,
                            "file_Submited" to tvFileName!!.text
                        )

                        // Query to save the data into "users" sub-collection of "submission" collection
                        newDocument!!.collection("users").document(userId).set(data)
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
                intent.putExtra("submissionId", newDocument!!.id)
                // Remove current activity history to prevent navigate back
                //intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_NO_HISTORY
                startActivity(intent)

                // Back to the previous Activity.
                finish()
            }
    }

    private fun selectImage(){
        val intent = Intent();

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
                    tvFileName!!.text = getFileNameFromUri(this, fileUrl!!)
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