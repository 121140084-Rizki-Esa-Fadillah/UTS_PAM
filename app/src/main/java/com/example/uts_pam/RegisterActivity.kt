package com.example.uts_pam

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.firestore
import java.util.regex.Pattern

class RegisterActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var img1: ImageView
    private lateinit var login: TextView

    private lateinit var edtFullName: EditText
    private lateinit var edtGithubName: EditText
    private lateinit var edtNik: EditText
    private lateinit var edtEmail: EditText
    private lateinit var edtPassword: EditText
    private lateinit var cnfPassword: EditText

    private val usersRef: DatabaseReference = FirebaseDatabase.getInstance().reference.child("users")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_register)

        img1 = findViewById(R.id.R_img_1)
        btnBackRegisterListener()

        login = findViewById(R.id.Register_to_Login)
        txtToLoginListener()

        edtFullName = findViewById(R.id.edt_fullname)
        edtGithubName = findViewById(R.id.edt_githubname)
        edtNik = findViewById(R.id.edt_nik)
        edtEmail = findViewById(R.id.edt_email)
        edtPassword = findViewById(R.id.edt_password)
        cnfPassword = findViewById(R.id.cnf_password)

        val submitButton: Button = findViewById(R.id.R_btn_1)
        submitButton.setOnClickListener {
            val fullName = edtFullName.text.toString()
            val githubName = edtGithubName.text.toString()
            val nik = edtNik.text.toString()
            val email = edtEmail.text.toString()
            val password = edtPassword.text.toString()
            val confirmPassword = cnfPassword.text.toString()

            val finalEmail = if (email == "admin") "admin@example.com" else email

            auth = FirebaseAuth.getInstance()
            val db = Firebase.firestore

            if (!isValidEmail(finalEmail)) {
                Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            auth.createUserWithEmailAndPassword(finalEmail, password)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user: FirebaseUser? = auth.currentUser
                        val userId = user?.uid

                        userId?.let { uid ->
                            val userData = hashMapOf(
                                "fullName" to fullName,
                                "email" to finalEmail,
                                "githubName" to githubName,
                                "nik" to nik,
                                "uid" to uid
                            )

                            db.collection("users").document(uid)
                                .set(userData)
                                .addOnSuccessListener { documentReference -> android.util.Log.d(TAG, "DocumentSnapshot added with ID:") }
                                .addOnFailureListener { e -> android.util.Log.w(TAG, "Error adding document", e) }

                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        }
                    } else {
                        if (task.exception is FirebaseAuthUserCollisionException) {
                            Toast.makeText(baseContext, "This email is already registered.", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(baseContext, "Registration failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                            Log.e("Registration", "Registration failed: ${task.exception?.message}")
                        }
                    }
                }
        }
    }

    private fun btnBackRegisterListener() {
        img1.setOnClickListener {
            startActivity(Intent(this, MotionLayoutActivity::class.java))
        }
    }

    private fun txtToLoginListener() {
        login.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun isValidEmail(email: String): Boolean {
        val emailPattern = Pattern.compile("^(.+)@(.+)$")
        val standardEmailPattern = Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")

        if (emailPattern.matcher(email).matches()) {
            return true
        } else if (standardEmailPattern.matcher(email).matches()) {
            return true
        }
        return false
    }
}
