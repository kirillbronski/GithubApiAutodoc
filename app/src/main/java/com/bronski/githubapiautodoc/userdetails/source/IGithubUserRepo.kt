package com.bronski.githubapiautodoc.userdetails.source

import com.bronski.githubapiautodoc.core.utils.BaseResult

interface IGithubUserRepo {
    suspend fun getUserDetails(username: String): BaseResult
}
