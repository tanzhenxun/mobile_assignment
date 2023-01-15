package com.example.project.supervisor.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.supervisor.ViewSubmission
import com.example.project.supervisor.adapter.ViewSubmissionAdapter
import com.google.firebase.firestore.FirebaseFirestore

class CompletedFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var submissionList: ArrayList<ViewSubmission>
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_completed, container, false)
        val itemview = inflater.inflate(R.layout.submission_item_list, container, false)
        view.requestLayout()

        recyclerView =view.findViewById(R.id.recyclerview_completed)
        recyclerView.layoutManager = LinearLayoutManager(context)

        submissionList = arrayListOf()

        val submissionReference = db.collection("submission")

        // Find student submission query
        submissionReference.get().addOnSuccessListener { submissionSnapshot ->
            // Check if inside submission is not empty
            if(!submissionSnapshot.isEmpty){
                // For loop document inside submission
                for(document in submissionSnapshot){
                    // retrieve the data of the current document
                    // Get data from users subcollection inside submission collection

                    // Approved, Rejected
                    val submitUserReference = document.reference.collection("users")


                    submitUserReference.whereEqualTo("submission_status", "Approved").get()
                        .addOnSuccessListener{ usersSnapshot ->
                            // Check if document is not empty
                            if(!usersSnapshot.isEmpty){
                                for(data in usersSnapshot){
                                    val submission: ViewSubmission? =
                                        data.toObject(ViewSubmission::class.java)
                                    if (submission != null) {
                                        submissionList.add(submission)
                                    }
                                }
                                recyclerView.adapter = ViewSubmissionAdapter(submissionList)
                            }
                        }

                    submitUserReference.whereEqualTo("submission_status", "Rejected").get()
                        .addOnSuccessListener{ usersSnapshot ->
                            // Check if document is not empty
                            if(!usersSnapshot.isEmpty){
                                for(data in usersSnapshot){
                                    val submission: ViewSubmission? =
                                        data.toObject(ViewSubmission::class.java)
                                    if (submission != null) {
                                        submissionList.add(submission)
                                    }
                                }
                                recyclerView.adapter = ViewSubmissionAdapter(submissionList)
                            }
                        }
                }
            }
        }

        return view
    }
}