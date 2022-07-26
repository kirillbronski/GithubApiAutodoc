package com.bronski.githubapiautodoc.userdetails.source.network

import com.bronski.githubapiautodoc.core.api.GithubApi
import com.bronski.githubapiautodoc.core.utils.BaseResult
import javax.inject.Inject

class GithubUserRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
) : IGithubUserRepo {

    override suspend fun getUserDetails(username: String): BaseResult =
        runCatching {
            githubApi.getUserInfo(username)
        }.fold(onSuccess = {
            BaseResult.Success(it)
        }, onFailure = {
            BaseResult.Error(it.message.toString())
        })

}