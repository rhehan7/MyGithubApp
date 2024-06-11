package com.dicoding.mygithubapp.ui.follow

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.remote.response.UsersGithubItem
import com.dicoding.mygithubapp.data.remote.retrofit.ApiConfig
import com.dicoding.mygithubapp.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {
    private val _listUsers = MutableLiveData<List<UsersGithubItem>>()
    val listUsers: LiveData<List<UsersGithubItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _messageEvent = MutableLiveData<Event<String>>()
    val messageEvent: LiveData<Event<String>> = _messageEvent

    companion object {
        private const val TAG = "FollowViewModel"
    }

    fun getFollowers(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(username)
        client.enqueue(object : Callback<List<UsersGithubItem>> {
            override fun onResponse(
                call: Call<List<UsersGithubItem>>,
                response: Response<List<UsersGithubItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body() ?: emptyList()
                } else {
                    _messageEvent.value = Event("Terjadi kesalahan saat memuat data")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersGithubItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }

    fun getFollowing(username: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(username)
        client.enqueue(object : Callback<List<UsersGithubItem>> {
            override fun onResponse(
                call: Call<List<UsersGithubItem>>,
                response: Response<List<UsersGithubItem>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _listUsers.value = response.body() ?: emptyList()
                } else {
                    _messageEvent.value = Event("Terjadi kesalahan saat memuat data")
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<UsersGithubItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "OnFailure : ${t.message}")
            }
        })
    }
}