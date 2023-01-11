package com.example.project.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProposalFragment : Fragment()  {

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
        val view = inflater.inflate(R.layout.activity_proposal_work, container, false)
        val itemView = inflater.inflate(R.layout.item_layout, container, false)
        recyclerView = view.findViewById(R.id.recycleview_result)
        recyclerView.layoutManager = LinearLayoutManager(context)



        submissionList = arrayListOf()

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users").document(userId).get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                val userData = userSnapshot.data
                val studentBatch = userData?.get("batch") as String

                val submissionReference2 = db.collection("submission")

                val submissionReference = submissionReference2.whereEqualTo("label", "Proposal Report")
                    .whereEqualTo("batch", studentBatch)



                submissionReference.get().addOnSuccessListener { submissionSnapshot ->

                    // create a list to store the data of the subcollection
//                    val dataList = mutableListOf<Map<String, Any>>()
//                    dataList.clear()

                    // store the data of the first document in a variable
//                            val parentData = submissionSnapshot.first().data
//                            Toast.makeText(context, dataList.joinToString(""), Toast.LENGTH_LONG).show()

                    for (document in submissionSnapshot) {
                        // retrieve the data of the current document
                        val currentDocumentId = document.id

                        document.reference.collection("users").document(userId).get()
                            .addOnSuccessListener { subcollectionSnapshot ->
                                if (subcollectionSnapshot.exists()) {
                                    // store the loop data for subcollection in a variable
                                    val parentData = document.data

                                    // Toast.makeText(context, parentData.toString(), Toast.LENGTH_LONG).show()

                                    // store the parentData data for subcollection in the list
//                                    dataList.add(parentData)

                                    // Process the subdocument
                                    val submission: Submission? =
                                        subcollectionSnapshot.toObject(Submission::class.java)
                                    if (submission != null) {
                                        submissionList.add(submission)
                                    }
                                    recyclerView.adapter = ProposalAdapter(submissionList)
                                } else {

                                    submissionReference2.document(currentDocumentId).get()
                                        .addOnSuccessListener { submissionSnapshot2 ->
                                            val submission: Submission? =
                                                submissionSnapshot2.toObject(Submission::class.java)
                                            if (submission != null) {
                                                submissionList.add(submission)
                                            }
                                            recyclerView.adapter = ProposalAdapter(submissionList)
                                        }
                                }
                            }
                    }
                }
                val submissionReference3 = submissionReference2.whereEqualTo("label", "Proposal PPT")
                    .whereEqualTo("batch", studentBatch)

                submissionReference3.get().addOnSuccessListener { submissionSnapshot ->

                    // create a list to store the data of the subcollection
//                    val dataList = mutableListOf<Map<String, Any>>()
//                    dataList.clear()

                    // store the data of the first document in a variable
//                            val parentData = submissionSnapshot.first().data
//                            Toast.makeText(context, dataList.joinToString(""), Toast.LENGTH_LONG).show()

                    for (document in submissionSnapshot) {
                        // retrieve the data of the current document
                        val currentDocumentId = document.id

                        document.reference.collection("users").document(userId).get()
                            .addOnSuccessListener { subcollectionSnapshot ->
                                if (subcollectionSnapshot.exists()) {
                                    // store the loop data for subcollection in a variable
                                    val parentData = document.data

                                    // Toast.makeText(context, parentData.toString(), Toast.LENGTH_LONG).show()

                                    // store the parentData data for subcollection in the list
//                                    dataList.add(parentData)

                                    // Process the subdocument
                                    val submission: Submission? =
                                        subcollectionSnapshot.toObject(Submission::class.java)
                                    if (submission != null) {
                                        submissionList.add(submission)
                                    }
                                    recyclerView.adapter = ProposalAdapter(submissionList)
                                } else {

                                    submissionReference2.document(currentDocumentId).get()
                                        .addOnSuccessListener { submissionSnapshot2 ->
                                            val submission: Submission? =
                                                submissionSnapshot2.toObject(Submission::class.java)
                                            if (submission != null) {
                                                submissionList.add(submission)
                                            }
                                            recyclerView.adapter = ProposalAdapter(submissionList)
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