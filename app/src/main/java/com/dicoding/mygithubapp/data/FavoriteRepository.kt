package com.dicoding.mygithubapp.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser
import com.dicoding.mygithubapp.data.local.room.FavoriteUserDao
import com.dicoding.mygithubapp.data.local.room.FavoriteUserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val mFavoriteDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavoriteUserRoomDatabase.getDatabase(application)
        mFavoriteDao = db.favoriteUserDao()
    }

    fun getFavoriteUser(): LiveData<List<FavoriteUser>> = mFavoriteDao.getFavoriteUser()

    fun getFavoriteByUsername(usename: String): LiveData<FavoriteUser> =
        mFavoriteDao.getFavoriteByUsername(usename)

    fun insertFavorite(user: FavoriteUser) {
        executorService.execute { mFavoriteDao.insertFavorite(user) } // run in background
    }

    fun deleteFavorite(user: FavoriteUser) {
        executorService.execute { mFavoriteDao.deleteFavorite(user) }
    }
}