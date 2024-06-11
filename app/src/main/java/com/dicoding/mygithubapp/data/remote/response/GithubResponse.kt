package com.dicoding.mygithubapp.data.remote.response

import com.google.gson.annotations.SerializedName

data class GithubResponse(

    @field:SerializedName("total_count")
    val totalCount: Int,

    @field:SerializedName("incomplete_results")
    val incompleteResults: Boolean,

    @field:SerializedName("items")
    val items: List<UsersGithubItem>
)

data class UsersGithubItem(

    @field:SerializedName("avatar_url")
    val avatarUrl: String,

    @field:SerializedName("login")
    val login: String
)
