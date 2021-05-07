package com.billyindrai.aplikasigithubuser

import android.content.ContentProvider
import android.content.ContentValues
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.billyindrai.aplikasigithubuser.database.Database
import com.billyindrai.aplikasigithubuser.database.DatabaseDAO

class Provider : ContentProvider() {

    private lateinit var databaseDao: DatabaseDAO

    companion object {
        private const val AUTHORITY = "com.billyindrai.aplikasigithubuser"
        private const val TABLE_NAME = "favorite"

        private const val FAV = 1
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)

        init {
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, FAV)
        }
    }

    override fun onCreate(): Boolean {
        databaseDao = context?.let { ctx ->
            Database.getDatabase(ctx)?.databaseDAO()
        }!!
        return false
    }

    override fun query(
        uri: Uri,
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor? {
        val cursor : Cursor?
        when(sUriMatcher.match(uri)){
            FAV -> {
                cursor = databaseDao.findUsers()
                if (context != null){
                    cursor.setNotificationUri(context?.contentResolver, uri)
                }
            }
            else -> {
                cursor = null
            }
        }
        return cursor
    }

    override fun getType(uri: Uri): String? {
        return null
    }

    // Implements the provider's insert method
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        return null
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        return  0
    }

    override fun update(
        uri: Uri,
        values: ContentValues?,
        selection: String?,
        selectionArgs: Array<out String>?
    ): Int {
        return 0
    }

}