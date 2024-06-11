package com.dicoding.mygithubapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class DetailUserResponse(

    @field:SerializedName("followers")
    val followers: Int,

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("following")
    val following: Int,

    @field:SerializedName("name")
    val name: Any?,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("login")
    val login: String
)
