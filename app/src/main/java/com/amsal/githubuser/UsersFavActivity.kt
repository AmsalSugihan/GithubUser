package com.amsal.githubuser

import android.content.Intent
import android.database.ContentObserver
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsal.githubuser.adapter.UserAdapter
import com.amsal.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users_fav)

        supportActionBar?.title = "Favorite User"

        rv_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_user_favorite.setHasFixedSize(true)
        adapter = UserAdapter()
        rv_user_favorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        loadUsersAsync()

        adapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: User) {
                showSelectedUser(data)
            }
        })

    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
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

    private fun showSelectedUser(user: User) {
        val intent = Intent(this, DetailUserActivity::class.java)
        intent.putExtra(DetailUserActivity.EXTRA_USER, user)
        startActivity(intent)
    }
}
