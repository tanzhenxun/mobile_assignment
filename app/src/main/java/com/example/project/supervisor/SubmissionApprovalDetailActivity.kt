package com.example.project.supervisor

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.project.R
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SubmissionApprovalDetailActivity : AppCompatActivity() {
    private lateinit var storageReference: StorageReference
    private lateinit var ref: StorageReference

    // Filename in Firebase
    var fileSub:String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_submission_approval_detail)

        // Declare variable for the data that get from previous activity
        val Label = intent.getStringExtra("Label")
        val studentName = intent.getStringExtra("studentName")
        val studentId = intent.getStringExtra("studentId")
        val title = intent.getStringExtra("title")
        val submissionStatus = intent.getStringExtra("submissionStatus")
        val abstract = intent.getStringExtra("abstract")
        val subDate = intent.getStringExtra("subDate")
        fileSub = intent.getStringExtra("fileSub")

        // Declare variable for view
        val IconTitle = findViewById<ImageView>(R.id.icon_title)
        val UserName = findViewById<TextView>(R.id.user_name)
        val StudentId = findViewById<TextView>(R.id.student_id)
        val Title = findViewById<TextView>(R.id.project_title)
        val SubmissionDate = findViewById<TextView>(R.id.submission_date)
        val ViewAbstract = findViewById<TextView>(R.id.tv_abstract)
        val Abstract = findViewById<TextView>(R.id.input_abstract)
        val SubmissionStatus = findViewById<TextView>(R.id.status)
        val ButtonDownload = findViewById<Button>(R.id.btn_download)

        // Store the data into view
        UserName.text = studentName
        StudentId.text = studentId
        Title.text = title
        SubmissionDate.text = subDate
        Abstract.text = abstract
        SubmissionStatus.text = submissionStatus

        ButtonDownload.setOnClickListener{
            download()
            Toast.makeText(this, "Pressed", Toast.LENGTH_LONG).show()
        }

        // If no title then textview disappear
        if (Title.text == ""){
            Title.visibility = View.GONE
        }

        if(Label == "Title"){
            ButtonDownload.visibility = View.GONE
        }else{
            ViewAbstract.text = "Student's Comment"
        }

        if(submissionStatus == "Approved") {
            val colorStateList = ContextCompat.getColorStateList(this, R.color.deep_green)
            SubmissionStatus.setBackgroundTintList(colorStateList)
        }else{} // Remain default color
    }

    private fun download(){
        storageReference = FirebaseStorage.getInstance().reference
        ref = storageReference.child("uploadedFile/" + fileSub)
        ref.downloadUrl.addOnSuccessListener { uri ->
            val url = uri.toString()
            Toast.makeText(baseContext, "Success",
                Toast.LENGTH_LONG).show()
            downloadFiles(this, fileSub!!, ".pdf", Environment.DIRECTORY_DOWNLOADS, url)
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