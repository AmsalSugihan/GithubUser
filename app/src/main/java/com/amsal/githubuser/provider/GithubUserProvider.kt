package com.amsal.githubuser.provider

import android.content.ContentProvider
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.net.Uri
import com.amsal.githubuser.db.DatabaseContract.AUTHORITY
import com.amsal.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.amsal.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME
import com.amsal.githubuser.db.UserHelper
import com.amsal.githubuser.model.User

class GithubUserProvider : ContentProvider() {
    companion object {
        private const val USER = 1
        private const val USER_ID = 2
        private val sUriMatcher = UriMatcher(UriMatcher.NO_MATCH)
        private lateinit var userHelper: UserHelper

        init {
            // content://com.dicoding.picodiploma.mynotesapp/note
            sUriMatcher.addURI(AUTHORITY, TABLE_NAME, USER)
            // content://com.dicoding.picodiploma.mynotesapp/note/id
            sUriMatcher.addURI(
                AUTHORITY,
                "$TABLE_NAME/#",
                USER_ID
            )
        }
    }

    override fun onCreate(): Boolean {
        userHelper = UserHelper.getInstance(context as Context)
        userHelper.open()
        return true
    }

    /*
    Method queryAll digunakan ketika ingin menjalankan queryAll Select
    Return cursor
     */
    override fun query(
        uri: Uri,
        strings: Array<String>?,
        s: String?,
        strings1: Array<String>?,
        s1: String?
    ): Cursor? {
        val cursor: Cursor?
        when (sUriMatcher.match(uri)) {
            USER -> cursor = userHelper.queryAll()
            USER_ID -> cursor = userHelper.queryById(uri.lastPathSegment.toString())
            else -> cursor = null
        }

        return cursor
    }


    override fun getType(uri: Uri): String? {
        return null
    }


    override fun insert(uri: Uri, contentValues: ContentValues?): Uri? {
        val added: Long = when (USER) {
            sUriMatcher.match(uri) -> userHelper.insert(contentValues)
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return Uri.parse("$CONTENT_URI/$added")
    }


    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String>?
    ): Int {
        val updated: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.update(
                uri.lastPathSegment.toString(),
                contentValues
            )
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return updated
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String>?): Int {
        val deleted: Int = when (USER_ID) {
            sUriMatcher.match(uri) -> userHelper.deleteById(uri.lastPathSegment.toString())
            else -> 0
        }

        context?.contentResolver?.notifyChange(CONTENT_URI, null)

        return deleted
    }
}
