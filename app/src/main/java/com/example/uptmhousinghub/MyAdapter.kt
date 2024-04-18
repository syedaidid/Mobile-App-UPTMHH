package com.example.uptmhousinghub

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MyAdapter(private val postlist: ArrayList<Posts>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnClickItemListener(clickListener: onItemClickListener) {
        mListener = clickListener
    }

    private lateinit var dbRef: DatabaseReference

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.post_item, parent, false)
        return MyViewHolder(itemView, mListener)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentItem = postlist[position]

        getUsernameFromUserId(currentItem.userId ?: "", holder.username)
        getPhoneFromUserId(currentItem.userId ?: "", holder.phone)

        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        holder.deposit.text = currentItem.deposit.toString()
        holder.monthly.text = currentItem.monthly.toString()
        holder.furnished.text = currentItem.furnished
        holder.facilities.text = currentItem.facilities
        holder.number.text = currentItem.numberOfPeople
        holder.gender.text = currentItem.gender
    }

    override fun getItemCount(): Int {
        return postlist.size
    }


    private fun getUsernameFromUserId(userId: String, usernameTextView: TextView) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                usernameTextView.text = user?.username ?: "Unknown User"
            }
            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }

    private fun getPhoneFromUserId(userId: String, phoneTextView: TextView) {
        val userRef = FirebaseDatabase.getInstance().getReference("Users").child(userId)
        userRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val user = snapshot.getValue(Users::class.java)
                phoneTextView.text = user?.phone ?: "Unknown Phone"
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle onCancelled event
            }
        })
    }

    class MyViewHolder(itemView: View, clickListener: onItemClickListener): RecyclerView.ViewHolder(itemView) {

        val title: TextView = itemView.findViewById(R.id.tvTitle)
        val description: TextView = itemView.findViewById(R.id.tvDescription)
        val deposit: TextView = itemView.findViewById(R.id.tvDeposit)
        val monthly: TextView = itemView.findViewById(R.id.tvMonthly)
        val furnished: TextView = itemView.findViewById(R.id.tvFurnished)
        val facilities: TextView = itemView.findViewById(R.id.tvFacilities)
        val number: TextView = itemView.findViewById(R.id.tvNumber)
        val gender: TextView = itemView.findViewById(R.id.tvGender)
        val username: TextView = itemView.findViewById(R.id.tvUsername)
        val phone: TextView = itemView.findViewById(R.id.tvPhone) // Add this line



        init {
            itemView.setOnClickListener {
                clickListener.onItemClick(adapterPosition)
            }
        }

    }
}