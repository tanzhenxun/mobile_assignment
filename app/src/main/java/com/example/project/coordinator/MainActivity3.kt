package com.example.project.coordinator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.example.project.R
import com.example.project.databinding.ActivityMain3Binding

class MainActivity3 : AppCompatActivity() {
    private lateinit var binding: ActivityMain3Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMain3Binding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(UserListFragment()) // Whenever open this page will show HomeFragment

        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.endorsement -> replaceFragment(UserListFragment())
                R.id.submission -> replaceFragment(AddSubmissionFragment())
                else -> {}
            }
            true
        }
    }


    private fun replaceFragment(fragment: Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout3, fragment)
        fragmentTransaction.commit()
    }
}