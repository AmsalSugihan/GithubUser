package com.amsal.githubuser.db

import android.net.Uri
import android.provider.BaseColumns

object DatabaseContract {

    const val AUTHORITY = "com.amsal.githubuser"
    const val SCHEME = "content"

    class UserColumns : BaseColumns {
        companion object {
            const val TABLE_NAME = "user_favorite"
            const val _ID = "_id"
            const val ID = "id_user"
            const val NAME = "name"
            const val AVATAR = "avatar"
            const val USERNAME = "username"
            const val FOLLOWING_URL = "following_url"
            const val FOLLOWERS_URL = "followers_url"
            const val URL = "url"

            val CONTENT_URI: Uri = Uri.Builder().scheme(SCHEME)
                .authority(AUTHORITY)
                .appendPath(TABLE_NAME)
                .build()

        }
    }
}