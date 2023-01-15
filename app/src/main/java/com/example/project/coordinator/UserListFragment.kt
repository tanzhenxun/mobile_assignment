package com.example.project.coordinator

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class UserListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var markList: ArrayList<Mark>
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
        val view = inflater.inflate(R.layout.fragment_user_list, container, false)
        recyclerView = view.findViewById(R.id.recyclerview_user)
        recyclerView.layoutManager = LinearLayoutManager(context)

        markList = arrayListOf()

        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        val markReference = db.collection("mark")

        markReference.get().addOnSuccessListener { markSnapshot ->
            if(!markSnapshot.isEmpty){
                for(document in markSnapshot){
                    val mark: Mark? =
                        document.toObject(Mark::class.java)
                    if (mark != null) {
                        markList.add(mark)
                    }
                    recyclerView.adapter = UserListAdapter(markList)
                }
            }
        }
//        val submissionReference = db.collection("submission")

//        submissionReference.get().addOnSuccessListener { submissionSnapshot ->
//            if (!submissionSnapshot.isEmpty) {
//                for (document in submissionSnapshot) {
//                    document.reference.collection("users").whereEqualTo("verify_status", "Pending")
//                        .get().addOnSuccessListener { userSnapshot ->
//                            if (!userSnapshot.isEmpty()) {
//                                for (data in userSnapshot) {
//                                    val mark: Mark? =
//                                        data.toObject(Mark::class.java)
//                                    if (mark != null) {
//                                        markList.add(mark)
//                                    }
//                                    recyclerView.adapter = UserListAdapter(markList)
//                                }
//                            }
//                        }
//                }
//            }
//        }

        return view
    }
}