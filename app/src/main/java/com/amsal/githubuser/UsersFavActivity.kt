package com.amsal.githubuser

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsal.githubuser.adapter.UserAdapter
import com.amsal.githubuser.db.UserHelper
import com.amsal.githubuser.model.User
import com.amsal.mynotesapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_users_fav.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UsersFavActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter
    private lateinit var userHelper: UserHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_fav)

        supportActionBar?.title = "Favorite User"

        rv_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_user_favorite.setHasFixedSize(true)
        adapter = UserAdapter()
        rv_user_favorite.adapter = adapter

        userHelper = UserHelper.getInstance(applicationContext)
        userHelper.open()
        loadUsersAsync()
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = userHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            progressbar.visibility = View.INVISIBLE
            val users = deferredNotes.await()
            if (users.size > 0) {
                adapter.setData(users)
            } else {
                adapter.setData(ArrayList())
            }
        }
    }
}
