package com.dicoding.mygithubapp.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.dicoding.mygithubapp.data.local.entity.FavoriteUser

@Database(entities = [FavoriteUser::class], version = 1)
abstract class FavoriteUserRoomDatabase : RoomDatabase() {
	abstract fun favoriteUserDao(): FavoriteUserDao

	companion object{
		@Volatile
		private var INSTANCE: FavoriteUserRoomDatabase? = null

		@JvmStatic
		fun getDatabase(context: Context): FavoriteUserRoomDatabase {
			if (INSTANCE == null){
				synchronized(FavoriteUserRoomDatabase::class){
					INSTANCE = Room.databaseBuilder(context.applicationContext,
						FavoriteUserRoomDatabase::class.java, "fav_database")
						.build()
				}
			}
			return INSTANCE as FavoriteUserRoomDatabase
		}
	}
}