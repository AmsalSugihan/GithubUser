package com.amsal.githubuser.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.amsal.consumerapp.R
import com.amsal.githubuser.model.User
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.user_item.view.*


class UserAdapter : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private var onItemClickCallback: OnItemClickCallback? = null

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }

    private val mData = ArrayList<User>()
    fun setData(items: ArrayList<User>) {
        mData.clear()
        mData.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, position: Int): UserViewHolder {
        val mView =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.user_item, viewGroup, false)
        return UserViewHolder(mView)
    }

    override fun onBindViewHolder(userViewHolder: UserViewHolder, position: Int) {
        userViewHolder.bind(mData[position])
    }

    override fun getItemCount(): Int = mData.size

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(users: User) {
            with(itemView) {
                txt_username.text = users.login
                txt_id_user.text = users.id.toString()
                Glide.with(itemView.context)
                    .load(users.avatar)
                    .apply(RequestOptions().override(350, 550))
                    .into(img_photo)
            }
            itemView.setOnClickListener { onItemClickCallback?.onItemClicked(users) }
        }
    }

    interface OnItemClickCallback {
        fun onItemClicked(data: User)
    }
}