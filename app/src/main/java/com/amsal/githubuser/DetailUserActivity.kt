package com.amsal.githubuser

import android.content.ContentValues
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.amsal.githubuser.adapter.SectionsPagerAdapter
import com.amsal.githubuser.db.DatabaseContract
import com.amsal.githubuser.db.DatabaseContract.UserColumns.Companion.CONTENT_URI
import com.amsal.githubuser.db.UserHelper
import com.amsal.githubuser.model.MainViewModel
import com.amsal.githubuser.model.User
import com.amsal.mynotesapp.helper.MappingHelper
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail_user.*
import org.json.JSONObject
import kotlin.math.log

class DetailUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_USER = "extra_user"
    }

    private val user = User()
    private lateinit var uriWithId: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_user)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val userSearch = intent.getParcelableExtra(EXTRA_USER) as User

        getUser(userSearch.url.toString())

        val followersUrl = userSearch.followers_url.toString()
        val followingUrl = userSearch.following_url.toString()
        val sectionsPagerAdapter =
            SectionsPagerAdapter(this, supportFragmentManager, followersUrl, followingUrl)
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        fab_add.setOnClickListener {

            uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)
            val cursor = contentResolver.query(uriWithId, null, null, null, null)

            if (cursor != null) {
                var result = MappingHelper.mapCursorToObject(cursor)
                if (result != null) {
                    deleteUserFavorite()
                } else {
                    setUserFavorite()
                }
                cursor.close()
            } else {
                Toast.makeText(this, "gagal", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    private fun getUser(url: String) {
        showLoading(true)
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authentication", "token <d385475e3e956289d566497e8233f1b0ee439895>")
        client.get(url, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseString: String
            ) {
                try {
                    //parsing json
                    showLoading(false)
                    val result = (responseString)
                    val users = JSONObject(result)
                    user.id = users.getInt("id")
                    user.name = users.getString("name")
                    user.followers = users.getInt("followers")
                    user.following = users.getInt("following")
                    user.repository = users.getInt("public_repos")
                    user.login = users.getString("login")
                    user.company = users.getString("company")
                    user.location = users.getString("location")
                    user.avatar = users.getString("avatar_url")
                    user.followers_url = users.getString("followers_url")
                    val userLogin = users.getString("login")
                    val following_url = "https://api.github.com/users/$userLogin/following"
                    user.following_url = following_url
                    user.url = users.getString("url")
                    showUser()
                } catch (e: Exception) {
                    Log.d("Exception", e.message.toString())
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>,
                responseString: String,
                throwable: Throwable
            ) {
                Log.d("onFailure", throwable.message.toString())
            }
        })
    }

    private fun showUser() {
        txt_nama.text = user.name
        txt_followers.text = user.followers.toString()
        txt_following.text = user.following.toString()
        txt_repository.text = user.repository.toString()
        supportActionBar?.title = user.login
        txt_company.text = user.company
        txt_location.text = user.location
        Glide.with(this@DetailUserActivity)
            .load(user.avatar)
            .apply(RequestOptions().override(350, 550))
            .into(image_photo)

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + user.id)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            var result = MappingHelper.mapCursorToObject(cursor)
            if (result != null) {
                Glide.with(this@DetailUserActivity)
                    .load(R.drawable.ic_fav)
                    .apply(RequestOptions().override(350, 550))
                    .into(fab_add)
            } else {
                Glide.with(this@DetailUserActivity)
                    .load(R.drawable.ic_fav_border)
                    .apply(RequestOptions().override(350, 550))
                    .into(fab_add)
            }
            cursor.close()
        } else {
            Toast.makeText(this, "gagal", Toast.LENGTH_LONG).show()
        }
    }

    private fun setUserFavorite() {

        val values = ContentValues()
        values.put(DatabaseContract.UserColumns.NAME, user.name)
        values.put(DatabaseContract.UserColumns.ID, user.id)
        values.put(DatabaseContract.UserColumns.AVATAR, user.avatar)
        values.put(DatabaseContract.UserColumns.USERNAME, user.login)
        values.put(DatabaseContract.UserColumns.FOLLOWING_URL, user.following_url)
        values.put(DatabaseContract.UserColumns.FOLLOWERS_URL, user.followers_url)
        values.put(DatabaseContract.UserColumns.URL, user.url)

        Log.d("horas", CONTENT_URI.toString())

        contentResolver.insert(CONTENT_URI, values)

        Glide.with(this@DetailUserActivity)
            .load(R.drawable.ic_fav)
            .apply(RequestOptions().override(350, 550))
            .into(fab_add)
        Toast.makeText(this, "${user.name} telah ditambahkan ke favorit", Toast.LENGTH_LONG)
            .show()
    }

    private fun deleteUserFavorite() {
        val dialogTitle: String
        val dialogMessage: String

        dialogMessage = "Apakah anda yakin ingin menghapus ${user.name} dari favorit?"
        dialogTitle = "Hapus Note"

        val alertDialogBuilder = AlertDialog.Builder(this)

        alertDialogBuilder.setTitle(dialogTitle)
        alertDialogBuilder
            .setMessage(dialogMessage)
            .setCancelable(false)
            .setPositiveButton("Ya") { dialog, id ->
                var result = contentResolver.delete(uriWithId, null, null)
                if (result > 0) {
                    Glide.with(this@DetailUserActivity)
                        .load(R.drawable.ic_fav_border)
                        .apply(RequestOptions().override(350, 550))
                        .into(fab_add)
                    Toast.makeText(
                        this,
                        "${user.name} telah dihapus dari favorit",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(this, "Gagal menghapus data", Toast.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Tidak") { dialog, id -> dialog.cancel() }
        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

}