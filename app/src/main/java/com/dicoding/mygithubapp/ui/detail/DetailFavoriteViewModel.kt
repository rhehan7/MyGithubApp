package com.dicoding.mygithubapp.ui.detail

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.mygithubapp.data.FavoriteRepository
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser

class DetailFavoriteViewModel(application: Application) : ViewModel() {
    private val mFavoriteRepository: FavoriteRepository = FavoriteRepository(application)

    fun getFavoriteByUsername(username: String) =  mFavoriteRepository.getFavoriteByUsername(username)

    fun insertFavorite(user: FavoriteUser) = mFavoriteRepository.insertFavorite(user)

    fun deleteFavorite(user: FavoriteUser) = mFavoriteRepository.deleteFavorite(user)
}