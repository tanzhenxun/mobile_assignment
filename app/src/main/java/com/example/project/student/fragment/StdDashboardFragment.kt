package com.example.project.student.fragment

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.project.student.ItemObject
import com.example.project.R
import com.example.project.student.tab.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class StdDashboardFragment : Fragment() {
    private lateinit var work_result: TabLayout
    private lateinit var actionlist: ViewPager2
    private var dataitem: ItemObject? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            dataitem = it.getParcelable("mText")
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_home_dashboard, container, false)
        work_result = view.findViewById(R.id.work_result)
        actionlist = view.findViewById(R.id.actionlist)
        actionlist.adapter = ViewPagerAdapter(this) // Add submission_list(viewpager) to Adapter
        TabLayoutMediator(work_result, actionlist){ tab,index ->  // pass 2 argument and get 2 argument
            tab.text = when(index){
                //Prefer using if for binary conditions. Prefer using when if there are three or more options.
                0 -> {"Ongoing Work"} // Display tab title
                1 -> {"Result"}
                else -> {throw Resources.NotFoundException("Position Not Found")}
            }
        }.attach()
        //Instantiating a TabLayoutMediator will only create the mediator object,
        //Attach() will link the TabLayout and the ViewPager2 together

        return view
        // Return the layout
    }
}