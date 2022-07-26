package com.bronski.githubapiautodoc.core.api.data

import com.google.gson.annotations.SerializedName

data class GithubResponse(
    @SerializedName("items") val repositories: MutableList<Repository>,
)
