package com.example.uptmhousinghub

data class Posts(
    val postId:String?= null,
    val userId:String?= null,
    val title:String?= null,
    val description:String?= null,
    val deposit:Double?= null,
    val monthly:Double?= null,
    val furnished:String?= null,
    val facilities:String?= null,
    val numberOfPeople:String?= null,
    val gender:String?= null,
)
