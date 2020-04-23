package com.amsal.githubuser.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.request.RequestOptions
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import com.loopj.android.http.TextHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject
import kotlin.math.log

class MainViewModel : ViewModel() {

    private val listUsers = MutableLiveData<ArrayList<User>>()
    private val listFollowers = MutableLiveData<ArrayList<User>>()
    private val listFollowing = MutableLiveData<ArrayList<User>>()

    fun setUserSearch(username: String) {
        val listItems = ArrayList<User>()

        val url = "https://api.github.com/search/users?q=$username"
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
                    val result = (responseString)
                    val responseObject = JSONObject(result)
                    val list = responseObject.getJSONArray("items")

                    for (i in 0 until list.length()) {
                        val users = list.getJSONObject(i)
                        val user = User()
                        user.id = users.getInt("id")
                        user.login = users.getString("login")
                        user.avatar = users.getString("avatar_url")
                        user.url = users.getString("url")
                        user.followers_url = users.getString("followers_url")
                        val userLogin = users.getString("login")
                        val following_url = "https://api.github.com/users/$userLogin/following"
                        user.following_url = following_url

                        listItems.add(user)
                    }

                    listUsers.postValue(listItems)
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

    fun setUserFollowers(url: String) {
        val listItems = ArrayList<User>()
        val client = AsyncHttpClient()
        client.addHeader("User-Agent", "request")
        client.addHeader("Authentication", "token <242601a424709b54192c26af36d49240b64217ac>")
        client.get(url, object : TextHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseString: String
            ) {
                try {
                    //parsing json
                    val result = (responseString)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val users = responseArray.getJSONObject(i)
                        val user = User()
                        user.id = users.getInt("id")
                        user.login = users.getString("login")
                        user.avatar = users.getString("avatar_url")
                        user.url = users.getString("url")
                        listItems.add(user)
                    }

                    listFollowers.postValue(listItems)
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

    fun setUserFollowing(url: String) {
        val listItems = ArrayList<User>()
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
                    val result = (responseString)
                    val responseArray = JSONArray(result)
                    for (i in 0 until responseArray.length()) {
                        val users = responseArray.getJSONObject(i)
                        var user = User()
                        user.id = users.getInt("id")
                        user.login = users.getString("login")
                        user.avatar = users.getString("avatar_url")
                        user.url = users.getString("url")
                        listItems.add(user)
                    }

                    listFollowing.postValue(listItems)
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


    fun getUsers(): LiveData<ArrayList<User>> {
        return listUsers
    }

    fun getFollowers(): LiveData<ArrayList<User>> {
        return listFollowers
    }

    fun getFollowing(): LiveData<ArrayList<User>> {
        return listFollowing
    }

}