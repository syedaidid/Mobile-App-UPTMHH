package com.example.uptmhousinghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreatePostActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var title: EditText
    private lateinit var description: EditText
    private lateinit var deposit: EditText
    private lateinit var monthly: EditText
    private lateinit var furnished: EditText
    private lateinit var facilities: EditText
    private lateinit var number: EditText
    private lateinit var radioGroup: RadioGroup
    private lateinit var gender: String
    private lateinit var submit: Button
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_post)

        title = findViewById(R.id.eTTitle)
        description = findViewById(R.id.eTDesc)
        deposit = findViewById(R.id.eTDeposit)
        monthly = findViewById(R.id.eTMonthly)
        furnished= findViewById(R.id.eTFurnished)
        facilities = findViewById(R.id.eTFacilities)
        number = findViewById(R.id.eTNumber)
        radioGroup = findViewById(R.id.radioGroup)
        submit = findViewById(R.id.btnSubmitCreate)
        userId = intent.getStringExtra("userId").toString()

        Toast.makeText(this, userId, Toast.LENGTH_LONG).show()

        // Listen for changes in the radio group
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected radio button
            val radioButton: RadioButton = findViewById(checkedId)

            // Get the text of the selected radio button
            gender = radioButton.text.toString()
        }

        submit.setOnClickListener {
            saveData(userId, title.text.toString(), description.text.toString(), deposit.text.toString(), monthly.text.toString(), furnished.text.toString(), facilities.text.toString(), number.text.toString(), gender)
        }

    }

    private fun saveData(userId:String, title:String, desc:String, deposit:String, monthly:String, furnished:String, facilities:String, number: String, gender:String) {
        dbRef = FirebaseDatabase.getInstance().getReference("Posts")
        val postId = dbRef.push().key!!
        val em = Posts(postId, userId, title, desc, deposit.toDouble(), monthly.toDouble(), furnished, facilities, number, gender)

        dbRef.child(postId).setValue(em)
            .addOnCompleteListener {
                Toast.makeText(this, "Post Created", Toast.LENGTH_LONG).show()
                val i = Intent(this, HomeActivity::class.java)
                i.putExtra("userId", userId)
                startActivity(i)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Fail to Create Post", Toast.LENGTH_LONG).show()
            }

    }
}