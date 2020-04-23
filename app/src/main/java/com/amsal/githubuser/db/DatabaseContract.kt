package com.amsal.githubuser.db

import android.provider.BaseColumns

internal class DatabaseContract {

    internal class UserColumns : BaseColumns {
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
        }
    }
}