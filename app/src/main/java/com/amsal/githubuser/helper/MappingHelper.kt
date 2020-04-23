package com.amsal.mynotesapp.helper

import android.database.Cursor
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
                val url = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val followersUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                val followingUrl = getString(getColumnIndexOrThrow(DatabaseContract.UserColumns.USERNAME))
                favUsersList.add(User(id, username, avatar,url, followersUrl, followingUrl, nama))
            }
        }
        return favUsersList
    }
}