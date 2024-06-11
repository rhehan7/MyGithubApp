package com.dicoding.mygithubapp.ui.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.remote.response.GithubResponse
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.data.remote.retrofit.ApiConfig
import com.dicoding.mygithubapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {
    // encapsulation

    private val _listUser = MutableLiveData<List<UsersGithubItem>>()
    val listUser: LiveData<List<UsersGithubItem>> = _listUser

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _messageEvent = MutableLiveData<Event<String>>()
    val messageEvent: LiveData<Event<String>> = _messageEvent

    companion object {
        private const val TAG = "MainViewModel"
        private const val USER_NAME = "Arif"
    }

    init {
        findUser()
    }

    fun findUser(userName: String = USER_NAME) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(userName)
        client.enqueue(object : Callback<GithubResponse> {
            override fun onResponse(
                call: Call<GithubResponse>,
                response: Response<GithubResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val users = response.body()?.items
                    _listUser.value = users ?: listOf()
                    if (users.isNullOrEmpty()) {
                        _messageEvent.value =
                            Event("User tidak ditemukan") // message event use event wrapper
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
                Event("${t.message}")
            }
        })
    }
}