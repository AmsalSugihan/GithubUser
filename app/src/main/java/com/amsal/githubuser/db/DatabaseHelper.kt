package com.amsal.githubuser.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.amsal.githubuser.db.DatabaseContract.UserColumns.Companion.TABLE_NAME

internal class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "dbfavuserapp"
        private const val DATABASE_VERSION = 2
        private val SQL_CREATE_TABLE_USER = "CREATE TABLE $TABLE_NAME" +
                " (${DatabaseContract.UserColumns._ID} INTEGER PRIMARY KEY AUTOINCREMENT," +
                " ${DatabaseContract.UserColumns.ID} INTEGER NOT NULL," +
                " ${DatabaseContract.UserColumns.NAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.USERNAME} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.AVATAR} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.FOLLOWING_URL} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.FOLLOWERS_URL} TEXT NOT NULL," +
                " ${DatabaseContract.UserColumns.URL} TEXT NOT NULL)"
    }

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(SQL_CREATE_TABLE_USER)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}