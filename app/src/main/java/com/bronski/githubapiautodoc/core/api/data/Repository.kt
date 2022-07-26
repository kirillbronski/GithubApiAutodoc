package com.bronski.githubapiautodoc.core.api.data

import android.annotation.SuppressLint
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.*

data class Repository(
    @SerializedName("node_id") val nodeId: String,
    @SerializedName("description") val description: String,
    @SerializedName("language") val language: String,
    @SerializedName("name") val name: String,
    @SerializedName("owner") val owner: Owner,
    @SerializedName("stargazers_count") val stargazersCount: Int,
    @SerializedName("updated_at") val updatedAt: Date,
) {
    @SuppressLint("SimpleDateFormat")
    fun getUpdatedDateAsString(): String? {
        val formatter = SimpleDateFormat("dd.MM.yyyy");
        return formatter.format(updatedAt)
    }
}
