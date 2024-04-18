package com.example.uptmhousinghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast

class HomeActivity : AppCompatActivity() {

    private lateinit var newpost: Button
    private lateinit var postlist: Button
    private lateinit var userId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        newpost = findViewById(R.id.btnGoCreatePost)
        postlist = findViewById(R.id.btnGoPostListAdmin)
        userId = intent.getStringExtra("userId").toString()

        Toast.makeText(this, userId, Toast.LENGTH_LONG).show()

        newpost.setOnClickListener{
            val i = Intent(this, CreatePostActivity::class.java)
            i.putExtra("userId", userId) // Correct way to pass userId
            startActivity(i)
        }
        postlist.setOnClickListener{
            val i = Intent(this, PostListActivity::class.java)
            i.putExtra("userId", userId) // Correct way to pass userId
            startActivity(i)
        }
    }
}
