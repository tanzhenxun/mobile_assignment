package com.example.project.supervisor

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.example.project.R
import com.example.project.coordinator.MainActivity3
import com.example.project.student.MainActivity
import com.example.project.supervisor.fragment.GradeFragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        var email_login = findViewById<TextInputEditText>(R.id.email_login)
        var password_login = findViewById<TextInputEditText>(R.id.password_login)
        var btn_login = findViewById<Button>(R.id.btn_login)
        loginCheck()

        auth = FirebaseAuth.getInstance()

        btn_login.setOnClickListener{
            if(checking(email_login, password_login)){
                // Get text field from TextView
                // TextView.text is not a String but Editable, need to add .toString().
                val email = email_login.text.toString()
                val password = password_login.text.toString()

                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            login() // Login based on role

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(baseContext, "Please make sure your email and password is correct",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
            else{
                Toast.makeText(this, "Please enter the details", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checking(email_login: TextInputEditText, password_login:TextInputEditText):Boolean{
        if(email_login.text.toString().trim{it<=' '}.isNotEmpty()
            && (password_login.text.toString().trim{it<=' '}.isNotEmpty())){
            return true
        }
        return false
    }

    private fun login() { // Login based on role
        val firestore = FirebaseFirestore.getInstance()

        // Get the user's ID
        val userId = FirebaseAuth.getInstance().currentUser!!.uid

        // Get the user's document from the "users" collection
        val userDocument = firestore.collection("users").document(userId)
        userDocument.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val role = document.getString("role")
                    when (role) {
                        "student" -> {
                            // Save the login flag in SharedPreferences
                            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("studentLogIn", true)
                            editor.apply()

                            // Redirect to student dashboard
                            val intent = Intent(this, MainActivity::class.java)
                            startActivity(intent)
                            finish() // Close the current activity and remove it from the activity stack.
                        }
                        "supervisor" -> {
                            // Save the login flag in SharedPreferences
                            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("supervisorLogIn", true)
                            editor.apply()

                            // Redirect to supervisor dashboard
                            val intent = Intent(this, MainActivity2::class.java)
                            startActivity(intent)
                            finish() // Close the current activity and remove it from the activity stack.
                        }
                        "coordinator" -> {
                            // Save the login flag in SharedPreferences
                            val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)
                            val editor = sharedPreferences.edit()
                            editor.putBoolean("coordinatorLogIn", true)
                            editor.apply()

                            // Redirect to coordinator dashboard
                            val intent = Intent(this, MainActivity3::class.java)
                            startActivity(intent)
                            finish() // Close the current activity and remove it from the activity stack.
                        }
                        else -> {
                            // Handle invalid role
                            Toast.makeText(this, "You haven't assigned a role, Please contact the Administrator",
                                Toast.LENGTH_LONG).show()
                        }
                    }
                } else {
                    // Handle missing document
                }
            }
            .addOnFailureListener { exception ->
                // Handle get failure
            }
    }

    private fun loginCheck(){
        // Check the login flag in the main activity's onCreate() method
        val sharedPreferences = getSharedPreferences("login", Context.MODE_PRIVATE)

        if (sharedPreferences.getBoolean("studentLogIn", false)){
            // The user is already logged in, so go to the main app screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        } else if (sharedPreferences.getBoolean("supervisorLogIn", false)){
            // The user is not logged in, so show the login screen
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
            finish()
        } else if (sharedPreferences.getBoolean("coordinatorLogIn", false)){
            // The user is not logged in, so show the login screen
            val intent = Intent(this, MainActivity3::class.java)
            startActivity(intent)
            finish()
        } else {}
    }
}