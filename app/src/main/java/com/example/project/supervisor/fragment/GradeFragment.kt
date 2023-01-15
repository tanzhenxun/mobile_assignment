package com.example.project.supervisor.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.supervisor.Grade
import com.example.project.R
import com.example.project.supervisor.adapter.ViewGradeAdapter
import com.google.firebase.firestore.FirebaseFirestore

class GradeFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var gradeList: ArrayList<Grade>
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
        val view = inflater.inflate(R.layout.fragment_grade, container, false)
        val itemview = inflater.inflate(R.layout.submission_item_list, container, false)

        recyclerView = view.findViewById(R.id.recyclerview_pending)
        recyclerView.layoutManager = LinearLayoutManager(context)
        gradeList = arrayListOf()

        val submissionReference = db.collection("mark")

        submissionReference.get().addOnSuccessListener { submissionSnapshot ->
            if(!submissionSnapshot.isEmpty){
                for(data in submissionSnapshot){
                    Toast.makeText(context, "Success", Toast.LENGTH_LONG).show()
                    val grade: Grade? =
                        data.toObject(Grade::class.java)
                    if (grade != null) {
                        gradeList.add(grade)
                    }
                    recyclerView.adapter = ViewGradeAdapter(gradeList)
                }
            }
        }

        return view
    }
}