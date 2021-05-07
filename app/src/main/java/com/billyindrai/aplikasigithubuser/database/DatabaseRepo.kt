package com.billyindrai.aplikasigithubuser.database

import androidx.lifecycle.LiveData
import com.billyindrai.aplikasigithubuser.User

class DatabaseRepo (private val databaseDao: DatabaseDAO) {
    val users: LiveData<List<User>> = databaseDao.getAllUsers()
    var findUserDB: LiveData<User>? = null
    suspend fun insert(user: User) {
        databaseDao.insert(user)
    }

    fun findUser(username: String) : LiveData<User>? {
        findUserDB = databaseDao.findUser(username)
        return findUserDB
    }

    suspend fun delete(username: String) {
        databaseDao.delete(username)
    }
}