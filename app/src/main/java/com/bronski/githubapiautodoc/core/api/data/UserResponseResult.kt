package com.bronski.githubapiautodoc.core.api.data

import com.google.gson.annotations.SerializedName

data class UserResponseResult(
    @SerializedName("login") val login: String,
    @SerializedName("avatar_url") val avatarUrl: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("twitter_username") val twitterUsername: String?,
    @SerializedName("followers") val followers: Int,
    @SerializedName("following") val following: Int,
    @SerializedName("blog") val webSite: String,
)