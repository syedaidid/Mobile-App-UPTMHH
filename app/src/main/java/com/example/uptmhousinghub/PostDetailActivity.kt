package com.example.uptmhousinghub
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.database.FirebaseDatabase

class PostDetailActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDesc: TextView
    private lateinit var tvDeposit: TextView
    private lateinit var tvMonthly: TextView
    private lateinit var tvFurnished: TextView
    private lateinit var tvFacilities: TextView
    private lateinit var tvNumber: TextView
    private lateinit var tvGender: TextView
    private lateinit var btnUpdate: Button
    private lateinit var btnDelete: Button
    private lateinit var postId: String
    private lateinit var ownerId: String
    private lateinit var userId: String
    private lateinit var admin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_detail)

        tvTitle = findViewById(R.id.tvTitle1)
        tvDesc = findViewById(R.id.tvDescription)
        tvDeposit = findViewById(R.id.tvDeposit)
        tvMonthly = findViewById(R.id.tvMonthly)
        tvFurnished = findViewById(R.id.tvFurnished)
        tvFacilities = findViewById(R.id.tvFacilities)
        tvNumber = findViewById(R.id.tvNumber)
        tvGender = findViewById(R.id.tvGender)
        postId = intent.getStringExtra("postId").toString()
        ownerId = intent.getStringExtra("ownerId").toString()
        btnUpdate = findViewById(R.id.btnUpdate)
        btnDelete = findViewById(R.id.btnDelete)

        userId = intent.getStringExtra("userId").toString()
        admin = intent.getStringExtra("admin").toString()
        val deposit = intent.getStringExtra("deposit").toString()

        Toast.makeText(this, deposit, Toast.LENGTH_LONG).show()
        Toast.makeText(this, admin, Toast.LENGTH_LONG).show()

        setValuestoViews()

        // Check if the post is made by the current user
        if (ownerId == userId || admin == "1") {
            // Show update and delete buttons
            btnUpdate.visibility = View.VISIBLE
            btnDelete.visibility = View.VISIBLE

            btnUpdate.setOnClickListener {
                openUpdateDialog(
                    intent.getStringExtra("postId").toString(),
                    intent.getStringExtra("title").toString(),
                    intent.getStringExtra("description").toString(),
                    intent.getStringExtra("deposit").toString(),
                    intent.getStringExtra("monthly").toString(),
                    intent.getStringExtra("furnished").toString(),
                    intent.getStringExtra("facilities").toString(),
                    intent.getStringExtra("number").toString(),
                    intent.getStringExtra("gender").toString()
                )
            }

            btnDelete.setOnClickListener {
                deleteRecord(
                    intent.getStringExtra("postId").toString()
                )
            }
        } else {
            // Hide update and delete buttons
            btnUpdate.visibility = View.GONE
            btnDelete.visibility = View.GONE
        }
    }


    private fun deleteRecord(id:String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(id)
        val mTask = dbRef.removeValue()

        mTask.addOnSuccessListener {
            Toast.makeText(this, "Post delete sucessfully", Toast.LENGTH_LONG).show()

            val intent = Intent(this, PostListActivity::class.java)
            finish()
            startActivity(intent)
        }.addOnFailureListener{
            error -> Toast.makeText(this, "Post delete error", Toast.LENGTH_LONG).show()
        }
    }


    private fun setValuestoViews() {
        tvTitle.text = intent.getStringExtra("title")
        tvDesc.text = intent.getStringExtra("description")
        // Fixing the retrieval of deposit and monthly values
        val deposit = intent.getStringExtra("deposit")?.toDoubleOrNull() ?: 0.0
        val monthly = intent.getStringExtra("monthly")?.toDoubleOrNull() ?: 0.0

        // Converting Double values to String before setting them
        tvDeposit.text = deposit.toString()
        tvMonthly.text = monthly.toString()
        tvFurnished.text = intent.getStringExtra("furnished")
        tvFacilities.text = intent.getStringExtra("facilities")
        tvNumber.text = intent.getStringExtra("number")
        tvGender.text = intent.getStringExtra("gender")
    }


    private fun openUpdateDialog(
        postId: String,
        title: String,
        description: String,
        deposit: String,
        monthly: String,
        furnished: String,
        facilities: String,
        number: String,
        gender: String
    ) {
        val mDialog = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val mDialogView = inflater.inflate(R.layout.update_dialog, null)

        mDialog.setView(mDialogView)

        val editTitle = mDialogView.findViewById<EditText>(R.id.editTitle)
        val editDescription = mDialogView.findViewById<EditText>(R.id.editDescription)
        val editDeposit = mDialogView.findViewById<EditText>(R.id.editDeposit)
        val editMonthly = mDialogView.findViewById<EditText>(R.id.editMonthly)
        val editFurnished = mDialogView.findViewById<EditText>(R.id.editFurnished)
        val editFacilities = mDialogView.findViewById<EditText>(R.id.editFacilities)
        val editNumber = mDialogView.findViewById<EditText>(R.id.editNumber)
        val editGender = mDialogView.findViewById<EditText>(R.id.editGender)
        val btnUpdateData = mDialogView.findViewById<Button>(R.id.btnUpdateData)

        editTitle.setText(title)
        editDescription.setText(description)

        editDeposit.setText(deposit)
        editMonthly.setText(monthly)

        editFurnished.setText(furnished)
        editFacilities.setText(facilities)
        editNumber.setText(number)
        editGender.setText(gender)

        mDialog.setTitle("Updating $title post")

        val alertDialog = mDialog.create()
        alertDialog.show()

        btnUpdateData.setOnClickListener {
            updatePostData(
                postId,
                ownerId,
                editTitle.text.toString(),
                editDescription.text.toString(),
                editDeposit.text.toString().toDouble(),
                editMonthly.text.toString().toDouble(),
                editFurnished.text.toString(),
                editFacilities.text.toString(),
                editNumber.text.toString(),
                editGender.text.toString()
            )
            alertDialog.dismiss()
        }
    }


    private fun updatePostData (
        postId:String,
        ownerId: String,
        title: String,
        description: String,
        deposit: Double,
        monthly: Double,
        furnished: String,
        facilities: String,
        number: String,
        gender: String
    ) {
        val dbRef = FirebaseDatabase.getInstance().getReference("Posts").child(postId)
        val postInfo = Posts(postId, ownerId, title, description, deposit, monthly, furnished, facilities, number, gender)
        dbRef.setValue(postInfo)
            .addOnSuccessListener {
                // Data updated successfully
                Toast.makeText(this, "Post updated successfully", Toast.LENGTH_SHORT).show()
                val i = Intent(this, HomeActivity::class.java)
                i.putExtra("userId", userId)
                // Finish the current activity to prevent going back to it when pressing back button
                finish()
            }
            .addOnFailureListener { e ->
                // Error occurred while updating data
                Toast.makeText(this, "Failed to update post: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
