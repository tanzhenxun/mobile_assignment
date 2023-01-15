package com.example.project.supervisor

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment.DIRECTORY_DOWNLOADS
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.project.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SubmissionApprovalActivity : AppCompatActivity() {
    @SuppressLint("SetTextI18n")

//    class getMark {
//        companion object {
//            var proposal:Long?=null
//            var final:Long?=null
//        }
//    }

    private lateinit var storageReference: StorageReference
    private lateinit var ref: StorageReference

    // Filename in Firebase
    var fileSub:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_approval)

        // Declare variable for the data that get from previous activity
        val submissionId = intent.getStringExtra("submissionId")
        val Label = intent.getStringExtra("Label")
        val studentName = intent.getStringExtra("studentName")
        val studentId = intent.getStringExtra("studentId")
        val subDate = intent.getStringExtra("subDate")
        val title = intent.getStringExtra("title")
        val abstract = intent.getStringExtra("abstract")
        fileSub = intent.getStringExtra("fileSub")
        val UserId = intent.getStringExtra("UserId")

        // Declare variable for view
        val IconTitle = findViewById<ImageView>(R.id.icon_title)
        val UserName = findViewById<TextView>(R.id.user_name)
        val StudentId = findViewById<TextView>(R.id.student_id)
        val Title = findViewById<TextView>(R.id.title)
        val SubmissionDate = findViewById<TextView>(R.id.submission_date)
        val ViewAbstract = findViewById<TextView>(R.id.tv_abstract)
        val Abstract = findViewById<TextView>(R.id.input_abstract)
        val Feedback = findViewById<TextView>(R.id.input_feedback)
        val ButtonApprove = findViewById<Button>(R.id.btn_approve)
        val ButtonReject = findViewById<Button>(R.id.btn_reject)
        val fileName = findViewById<TextView>(R.id.fileName)
        val ButtonDownload = findViewById<Button>(R.id.btn_download)

        // Declare data for database variable
        val db = FirebaseFirestore.getInstance()

        UserName.text = studentName
        StudentId.text = studentId
        Title.text = title
        SubmissionDate.text = subDate
        Abstract.text = abstract
        fileName.text = fileSub

        // If no title then textview disappear
        if (Title.text == ""){
            Title.visibility = View.GONE
        }

        // Display button and text based on label
        if(Label == "Title"){
            ButtonDownload.visibility = View.GONE
            fileName.visibility = View.GONE
        }else{
            ViewAbstract.text = "Student's Comment"
        }

        ButtonDownload.setOnClickListener{
            download()
            Toast.makeText(this, "Pressed", Toast.LENGTH_LONG).show()
        }

        ButtonApprove.setOnClickListener {
            val feedback = Feedback.text.toString().trim()

            val submissionStatus = "Approved"

            val data1 = mapOf(
                "title" to title
            )

            if(Label == "Title"){
                db.collection("users").document(UserId!!).update(data1)
                    .addOnSuccessListener { successSnapshot ->
                    }
            }

            val data2 = mapOf(
                "feedback" to feedback,
                "submission_status" to submissionStatus
            )

            val submissionReference2 = db.collection("submission").document(submissionId.toString())

            submissionReference2.collection("users").document(UserId.toString())
                .update(data2).addOnSuccessListener{ documentSnapshot ->
                    // The data was successfully saved
                    Toast.makeText(baseContext, "Student work has been approve",
                        Toast.LENGTH_LONG).show()

                    val intent = Intent(this, SubmissionApprovalDetailActivity::class.java)
                    intent.putExtra("studentName", studentName)
                    intent.putExtra("studentId", studentId)
                    intent.putExtra("title", title)
                    intent.putExtra("submissionStatus", submissionStatus)
                    intent.putExtra("subDate", subDate)
                    intent.putExtra("abstract", abstract)
                    startActivity(intent)

                }
                .addOnFailureListener { e ->
                    // The save failed
                    Toast.makeText(baseContext, "Fail to approve, please try again",
                        Toast.LENGTH_LONG).show()
                }
        }

        ButtonReject.setOnClickListener {
            val feedback = Feedback.text.toString().trim()

            val submissionStatus = "Rejected"

            val data = mapOf(
                "feedback" to feedback,
                "submission_status" to submissionStatus
            )

            val submissionReference2 = db.collection("submission").document(submissionId.toString())

            submissionReference2.collection("users").document(UserId.toString())
                .update(data).addOnSuccessListener{ documentSnapshot ->
                    // The data was successfully saved
                    Toast.makeText(baseContext, "Student work has been rejected",
                        Toast.LENGTH_LONG).show()

                    val intent = Intent(this, SubmissionApprovalDetailActivity::class.java)
                    intent.putExtra("studentName", studentName)
                    intent.putExtra("studentId", studentId)
                    intent.putExtra("title", title)
                    intent.putExtra("submissionStatus", submissionStatus)
                    intent.putExtra("subDate", subDate)
                    intent.putExtra("abstract", abstract)
                    startActivity(intent)

                }
                .addOnFailureListener { e ->
                    // The save failed
                    Toast.makeText(baseContext, "Fail to reject, please try again",
                        Toast.LENGTH_LONG).show()
                }
        }
    }

    private fun download(){
        storageReference = FirebaseStorage.getInstance().reference
        ref = storageReference.child("uploadedFile/" + fileSub)
        ref.downloadUrl.addOnSuccessListener { uri ->
            val url = uri.toString()
            Toast.makeText(baseContext, "Success",
                Toast.LENGTH_LONG).show()
            downloadFiles(this, fileSub!!, ".pdf", DIRECTORY_DOWNLOADS, url)
        }.addOnFailureListener { e ->
            // handle failure
            Toast.makeText(baseContext, "Fail",
                Toast.LENGTH_LONG).show()
        }
    }

    private fun downloadFiles(context: Context, fileName: String, fileExtension: String,
                              destinationDirectory: String, url: String){
        val downloadManager: DownloadManager = context.
        getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager

        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)

        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalFilesDir(context, destinationDirectory, fileName)

        downloadManager.enqueue(request)
    }
}