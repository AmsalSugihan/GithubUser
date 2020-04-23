package com.amsal.githubuser.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
class User(
    var id: Int = 0,
    var login: String? = null,
    var avatar: String? = null,
    var url: String? = null,
    var followers_url: String? = null,
    var following_url: String? = null,
    var name: String? = null,
    var followers: Int = 0,
    var following: Int = 0,
    var repository: Int = 0,
    var company: String? = null,
    var location: String? = null
) : Parcelable