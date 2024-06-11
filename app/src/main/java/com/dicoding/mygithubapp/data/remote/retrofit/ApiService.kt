package com.dicoding.mygithubapp.data.remote.retrofit

import com.dicoding.mygithubapp.data.remote.response.DetailUserResponse
import com.dicoding.mygithubapp.data.remote.response.GithubResponse
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    // search
    @GET("search/users")
    fun getUsers(
        @Query("q") query: String
    ): Call<GithubResponse>

    // detail user
    @GET("users/{username}")
    fun getDetailUser(
        @Path("username") username: String
    ): Call<DetailUserResponse>

    // list followers
    @GET("users/{username}/followers")
    fun getFollowers(@Path("username") username: String): Call<List<UsersGithubItem>>

    // list following
    @GET("users/{username}/following")
    fun getFollowing(@Path("username") username: String): Call<List<UsersGithubItem>>
}