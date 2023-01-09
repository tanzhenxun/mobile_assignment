package com.example.project

import android.annotation.SuppressLint
import android.content.ContentValues.TAG
import android.icu.text.CaseMap.Title
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.project.databinding.ActivityMainBinding
import com.example.project.fragment.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Show Home Page whenever open the app
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.home_nav

        // When open the app will show HomeFragment
        replaceFragment(StdDashboardFragment())


        binding.bottomNavigationView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.home_nav -> replaceFragment(StdDashboardFragment())
                R.id.title_nav -> replaceFragment(TitleFragment())
                R.id.thesis_nav -> replaceFragment(ThesisFragment())
                R.id.poster_nav -> replaceFragment(PosterFragment())
                R.id.proposal_nav -> replaceFragment(ProposalFragment())

                else -> {}
            }
            true
        }

        val db = FirebaseFirestore.getInstance()
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        db.collection("users").document(userId).get().addOnSuccessListener { userSnapshot ->
            if (userSnapshot.exists()) {
                val last_name = userSnapshot.getString("last_name")
                val first_name = userSnapshot.getString("first_name")
                val student_id = userSnapshot.getString("std_id")

                val studentName = findViewById<TextView>(R.id.user_name)
                val studentId = findViewById<TextView>(R.id.user_id)
                studentName.text = first_name + " " + last_name
                studentId.text = student_id

//                 Log.d(TAG, "${userSnapshot.id} => ${userSnapshot.data}")
//                Toast.makeText(this, last_name + first_name + "\n" + student_id,
//                    Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }

}