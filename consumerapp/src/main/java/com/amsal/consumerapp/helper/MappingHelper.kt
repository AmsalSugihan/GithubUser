package com.amsal.mynotesapp.helper

import android.database.Cursor
import android.util.Log
import com.amsal.githubuser.db.DatabaseContract
import com.amsal.githubuser.model.User

object MappingHelper {

    fun mapCursorToArrayList(favUsersCursor: Cursor?): ArrayList<User> {
        val favUsersList = ArrayList<User>()
        favUsersCursor?.apply {
            while (moveToNext()) {
                val _id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL))
                val followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS_URL))
                val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING_URL))
                favUsersList.add(User(id, username, avatar,url, followersUrl, followingUrl, nama))
            }
        }
        return favUsersList
    }

    fun mapCursorToObject(notesCursor: Cursor?): User? {
        var user = User()
        try {
            notesCursor?.apply {
                moveToFirst()
                val _id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns._ID))
                val nama = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.NAME))
                val id = getInt(getColumnIndexOrThrow(DatabaseContract.UserColumns.ID))
                val avatar = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.AVATAR))
                val username = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.URL))
                val followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWERS_URL))
                val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.FOLLOWING_URL))
                user = User(id, username, avatar,url, followersUrl, followingUrl, nama)
            }
            return user
        }catch (e : Exception){
            return null
        }
    }
}