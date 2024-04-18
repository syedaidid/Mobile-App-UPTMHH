package com.example.uptmhousinghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginActivity : AppCompatActivity() {
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var firebaseDatabase: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        username = findViewById(R.id.eTName)
        password = findViewById(R.id.eTEmail)
        loginButton = findViewById(R.id.btnSubmitLogin)
        firebaseDatabase = FirebaseDatabase.getInstance()

        loginButton.setOnClickListener {
            val enteredUsername = username.text.toString()
            val enteredPassword = password.text.toString()

            if (enteredUsername.isNotEmpty() && enteredPassword.isNotEmpty()) {
                loginUser(enteredUsername, enteredPassword)
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        if (username == "admin" && password == "admin123") {
            // Admin login successful
            Toast.makeText(this@LoginActivity, "Admin Login Successful", Toast.LENGTH_SHORT).show()
            // Start the admin activity or perform admin-specific actions
            val intent = Intent(this@LoginActivity, AdminActivity::class.java)
            intent.putExtra("admin", "1")
            startActivity(intent)
            finish()
            return
        }

        val databaseReference = firebaseDatabase.reference.child("Users")
        databaseReference.orderByChild("username").equalTo(username)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (userSnapshot in dataSnapshot.children) {
                            val user = userSnapshot.getValue(Users::class.java)
                            if (user?.password == password) {
                                // Regular user login successful
                                Toast.makeText(this@LoginActivity, "Login Successful", Toast.LENGTH_SHORT).show()
                                val userId = userSnapshot.key
                                val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                                intent.putExtra("userId", userId)
                                startActivity(intent)
                                finish()
                                return
                            }
                        }

                        // Login failed
                        Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                    } else {
                        // User not found
                        Toast.makeText(this@LoginActivity, "User not found", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onCancelled(databaseError: DatabaseError) {
                    Toast.makeText(this@LoginActivity, "Database Error: ${databaseError.message}", Toast.LENGTH_SHORT).show()
                }
            })
    }

}