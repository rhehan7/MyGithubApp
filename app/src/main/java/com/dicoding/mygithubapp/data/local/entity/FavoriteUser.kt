package com.dicoding.mygithubapp.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite_user")
@Parcelize
data class FavoriteUser(
	@PrimaryKey(autoGenerate = false)
	@ColumnInfo(name = "login")
	val username: String = "",

	@ColumnInfo(name = "avatar_url")
	val avatarUrl: String? = null
): Parcelable
