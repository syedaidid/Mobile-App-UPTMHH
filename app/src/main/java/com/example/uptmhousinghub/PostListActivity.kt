package com.example.uptmhousinghub

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class PostListActivity : AppCompatActivity() {

    private lateinit var dbRef: DatabaseReference
    private lateinit var postRecyclerView: RecyclerView
    private lateinit var postArrayList: ArrayList<Posts>
    private lateinit var userId: String
    private lateinit var admin: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post_list)

        postRecyclerView = findViewById(R.id.postList)
        postRecyclerView.layoutManager = LinearLayoutManager(this)
        postRecyclerView.setHasFixedSize(true)

        userId = intent.getStringExtra("userId").toString()
        admin = intent.getStringExtra("admin").toString()

        Toast.makeText(this, admin, Toast.LENGTH_LONG).show()

        postArrayList = arrayListOf<Posts>()
        getPostData()

    }
    private fun getPostData() {
        dbRef = FirebaseDatabase.getInstance().getReference("Posts")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    // Clear the postArrayList before adding items
                    postArrayList.clear()

                    for (postSnapshot in snapshot.children) {
                        val post = postSnapshot.getValue(Posts::class.java)
                        postArrayList.add(post!!)
                    }

                    val mAdapter = MyAdapter(postArrayList)
                    postRecyclerView.adapter = mAdapter

                    mAdapter.setOnClickItemListener(object : MyAdapter.onItemClickListener {
                        override fun onItemClick(position: Int) {
                            val intent = Intent(this@PostListActivity, PostDetailActivity::class.java)
                            intent.putExtra("userId", userId)
                            intent.putExtra("postId", postArrayList[position].postId)
                            intent.putExtra("ownerId", postArrayList[position].userId)
                            intent.putExtra("title", postArrayList[position].title)
                            intent.putExtra("description", postArrayList[position].description)
                            intent.putExtra("deposit", postArrayList[position].deposit.toString())
                            Toast.makeText(this@PostListActivity, postArrayList[position].deposit.toString(), Toast.LENGTH_LONG).show()
                            intent.putExtra("monthly", postArrayList[position].monthly.toString())
                            intent.putExtra("furnished", postArrayList[position].furnished)
                            intent.putExtra("facilities", postArrayList[position].facilities)
                            intent.putExtra("number", postArrayList[position].numberOfPeople)
                            intent.putExtra("gender", postArrayList[position].gender)
                            intent.putExtra("admin", admin) // Pass the admin value
                            startActivity(intent)
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }


}