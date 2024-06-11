package com.dicoding.mygithubapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser

@Dao
interface FavoriteUserDao {
	@Insert(onConflict = OnConflictStrategy.IGNORE)
	fun insertFavorite(user: FavoriteUser)

	@Delete
	fun deleteFavorite(user: FavoriteUser)

	@Query("SELECT * FROM favorite_user")
	fun getFavoriteUser(): LiveData<List<FavoriteUser>>

	@Query("SELECT * FROM favorite_user WHERE login = :username")
	fun getFavoriteByUsername(username: String): LiveData<FavoriteUser>
}