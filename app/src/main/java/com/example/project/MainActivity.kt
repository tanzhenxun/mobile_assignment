package com.example.project

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.fragment.app.Fragment
import com.example.project.databinding.ActivityMainBinding
import com.example.project.fragment.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        //replaceFragment(StdDashboardFragment()) // Whenever open this page will show HomeFragment
        val testing = ItemObject("Testing1","Testing2","Testing3","Tesing4")
        var mBundle = Bundle()
        mBundle.putParcelable("mText",testing)

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){



               //R.id.home_nav -> replaceFragment(StdDashboardFragment())
                R.id.title_nav -> replaceFragment(TitleFragment(), mBundle)
                //R.id.thesis_nav -> replaceFragment(ThesisFragment())
                //R.id.poster_nav-> replaceFragment(PosterFragment())
                //R.id.proposal_nav-> replaceFragment(ProposalFragment())

                else -> {}
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment, mBundle: Bundle?) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragment.arguments = mBundle
        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }
}