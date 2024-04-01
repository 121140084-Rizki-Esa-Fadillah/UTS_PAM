package com.example.uts_pam

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfilUserActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var nameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var nikTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profil)

        // Initialize Firebase
        FirebaseApp.initializeApp(this)

        auth = FirebaseAuth.getInstance()

        val img1: ImageView = findViewById(R.id.exit_profil)
        img1.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        nameTextView = findViewById(R.id.usernameTextView)
        usernameTextView = findViewById(R.id.githubUsernameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        nikTextView = findViewById(R.id.NikTextView)

        val logoutButton: Button = findViewById(R.id.logoutButton)
        logoutButton.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
        }

        val currentUser = auth.currentUser
        val uid = currentUser?.uid
        db = FirebaseFirestore.getInstance()

        uid?.let {userId ->
            db.collection("users").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document.exists()) {
                        val fullName = document.getString("fullName") ?: "Data not found"
                        val email = document.getString("email") ?: ""
                        val githubName = document.getString("githubName") ?: ""
                        val nik = document.getString("nik") ?: ""

                        nameTextView.text = "Nama : $fullName"
                        emailTextView.text = "E-mail : $email"
                        usernameTextView.text = "Github : $githubName"
                        nikTextView.text = "NIK : $nik"
                    } else {
                        nameTextView.text = "Data not found"
                        emailTextView.text = ""
                        usernameTextView.text = ""
                        nikTextView.text = ""
                    }
                }
                .addOnFailureListener { exception ->
                    nameTextView.text = "Error: ${exception.message}"
                    emailTextView.text = ""
                    usernameTextView.text = ""
                    nikTextView.text = ""
                }
        }
    }
}
