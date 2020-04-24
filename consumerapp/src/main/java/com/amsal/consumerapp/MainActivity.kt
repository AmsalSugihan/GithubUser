package com.amsal.consumerapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.HandlerThread
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.amsal.githubuser.adapter.UserAdapter
import com.amsal.githubuser.db.DatabaseContract
import com.amsal.mynotesapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: UserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.title = "Favorite User"

        rv_user_favorite.layoutManager = LinearLayoutManager(this)
        rv_user_favorite.setHasFixedSize(true)
        adapter = UserAdapter()
        rv_user_favorite.adapter = adapter

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        loadUsersAsync()
    }

    private fun loadUsersAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            progressbar.visibility = View.VISIBLE
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(
                    DatabaseContract.UserColumns.CONTENT_URI,
                    null,
                    null,
                    null,
                    null
                )
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
