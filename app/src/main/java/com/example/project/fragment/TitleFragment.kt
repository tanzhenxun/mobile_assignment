package com.example.project.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project.ItemObject
import com.example.project.R
import com.example.project.tab.ItemAdapter

class TitleFragment: Fragment()  {
    private var param3: ItemObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param3 = it.getParcelable("mText")
        }
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_title_work, container, false)
        val testing = ItemObject("proposal","proposal","pending",null)
        val dashboardtopic = view.findViewById<TextView>(R.id.dashboard_topic)
        val icon = view.findViewById<TextView>(R.id.icon)

        val itemList = arrayListOf<ItemObject>()
        itemList.add(ItemObject("Title1", "Project Title1", "Submission Date1", "Pending"))
        itemList.add(ItemObject("Title2", "Project Title2", "Submission Date2", "Completed"))
        itemList.add(ItemObject("Title3", "Project Title3", "Submission Date3", "Pending"))
        itemList.add(ItemObject("Title4", "Project Title4", "Submission Date4", "Completed"))
        itemList.add(ItemObject("Title5", "Project Title5", "Submission Date5", "Pending"))

        val recyclerView = view.findViewById<RecyclerView>(R.id.recycleview_result)
        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = ItemAdapter(itemList)

        return view
    }

}