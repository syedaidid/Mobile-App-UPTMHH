package com.example.uptmhousinghub

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class RegisterActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var submit: Button
    private lateinit var username: EditText
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var phone: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        submit = findViewById(R.id.btnSubmitRegister)
        username = findViewById(R.id.eTUsername)
        name = findViewById(R.id.eTName)
        password = findViewById(R.id.eTPassword)
        phone = findViewById(R.id.eTPhone)
        email = findViewById(R.id.eTEmail)

        submit.setOnClickListener {
            val enteredUsername = username.text.toString().trim()
            val enteredName = name.text.toString().trim()
            val enteredEmail = email.text.toString().trim()
            val enteredPassword = password.text.toString().trim()
            val enteredPhone = phone.text.toString().trim()

            if (validateInputs(enteredUsername, enteredName, enteredEmail, enteredPassword, enteredPhone)) {
                saveData(enteredUsername, enteredName, enteredPassword, enteredPhone, enteredEmail)
            }
        }
    }

    private fun validateInputs(username: String, name: String, email: String, password: String, phone: String): Boolean {
        if (username.isEmpty() || name.isEmpty() || email.isEmpty() || password.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email address", Toast.LENGTH_SHORT).show()
            return false
        }

        if (password.length < 6) {
            Toast.makeText(this, "Password must be at least 6 characters long", Toast.LENGTH_SHORT).show()
            return false
        }

        // You can add more validation rules as needed

        return true
    }

    private fun saveData(username: String, name: String, password: String, phone: String, email: String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        val customerId = dbRef.push().key!!
        val user = Users(customerId, username, name, password, email, phone)

        dbRef.child(customerId).setValue(user)
            .addOnCompleteListener {
                Toast.makeText(this, "User registered successfully", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
