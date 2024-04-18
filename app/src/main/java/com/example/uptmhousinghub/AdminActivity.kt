package com.example.uptmhousinghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminActivity : AppCompatActivity() {
    private lateinit var postlist: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin)

        postlist = findViewById(R.id.btnGoPostListAdmin)
        postlist.setOnClickListener {
            val i = Intent(this, PostListActivity::class.java)
            i.putExtra("admin", "1")
            startActivity(i)
        }
    }
}