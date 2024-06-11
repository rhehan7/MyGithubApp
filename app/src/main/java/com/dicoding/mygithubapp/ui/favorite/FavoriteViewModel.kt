package com.dicoding.mygithubapp.ui.favorite

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.FavoriteRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavoriteUser() = mFavoriteRepository.getFavoriteUser()
}