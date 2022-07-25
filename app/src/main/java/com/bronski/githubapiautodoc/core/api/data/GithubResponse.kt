package com.bronski.githubapiautodoc.core.api.data

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val repo: MutableList<SearchResponseResult>,
    @SerializedName("total_count") val totalCount: Int
)
