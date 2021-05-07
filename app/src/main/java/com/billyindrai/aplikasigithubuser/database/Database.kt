package com.billyindrai.aplikasigithubuser.database


import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.billyindrai.aplikasigithubuser.User

@Database(entities = arrayOf(User::class), version = 1, exportSchema = false)
abstract  class Database  : RoomDatabase(){
    abstract fun databaseDAO(): DatabaseDAO

    companion object{
        @Volatile
        private var INSTANCE: com.billyindrai.aplikasigithubuser.database.Database? = null

        fun getDatabase(context: Context): com.billyindrai.aplikasigithubuser.database.Database? {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    com.billyindrai.aplikasigithubuser.database.Database::class.java,
                    "database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}