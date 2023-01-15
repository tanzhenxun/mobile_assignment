package com.example.project.supervisor.fragment

import android.content.res.Resources
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.project.R
import com.example.project.supervisor.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

class HomeFragment : Fragment() {
    private lateinit var nav_status: TabLayout
    private lateinit var submission_list: ViewPager2


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
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        nav_status = view!!.findViewById(R.id.nav_status)
        submission_list = view.findViewById(R.id.submission_list)
        submission_list.adapter = ViewPagerAdapter(this) // Add submission_list(viewpager) to Adapter
        TabLayoutMediator(nav_status, submission_list){ tab,index ->  // pass 2 argument and get 2 argument
            tab.text = when(index){
                //Prefer using if for binary conditions. Prefer using when if there are three or more options.
                0 -> {"Pending"} // Display tab title
                1 -> {"Completed"}
                else -> {throw Resources.NotFoundException("Position Not Found")}
            }
        }.attach()
        //Instantiating a TabLayoutMediator will only create the mediator object,
        //Attach() will link the TabLayout and the ViewPager2 together

        return view
        // Return the layout
    }
}