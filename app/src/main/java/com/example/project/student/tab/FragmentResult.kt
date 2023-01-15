package com.example.project.student.tab

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.example.project.student.Submission
import com.example.project.student.adapter.OngoingAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FragmentResult : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var submissionList: ArrayList<Submission>
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_poster_work, container, false)

        recyclerView = view.findViewById(R.id.recycleview_result)
        recyclerView.layoutManager = LinearLayoutManager(context)

        submissionList = arrayListOf()

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users").document(userId).get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                val userData = userSnapshot.data
                val studentBatch = userData?.get("batch") as String

                val submissionReference2 = db.collection("submission")

                val submissionReference = submissionReference2.whereEqualTo("batch", studentBatch)

                submissionReference.get().addOnSuccessListener { submissionSnapshot ->
                    for (document in submissionSnapshot) {
                        // retrieve the data of the current document
                        val currentDocumentId = document.id

                        document.reference.collection("users").document(userId).get()
                            .addOnSuccessListener { subcollectionSnapshot ->
                                if (subcollectionSnapshot.exists()) {
                                    val validateStatus = subcollectionSnapshot.getString("submission_status")

                                    // Will show either new or pending submission
                                    if (validateStatus == "Approved" || validateStatus == "Rejected"){
                                        // Process the subdocument
                                        val submission: Submission? =
                                            subcollectionSnapshot.toObject(Submission::class.java)
                                        if (submission != null) {
                                            submissionList.add(submission)
                                        }
                                        recyclerView.adapter = OngoingAdapter(submissionList)
                                    }
                                }
                            }
                    }
                }

            }
        }

        return view
    }
}