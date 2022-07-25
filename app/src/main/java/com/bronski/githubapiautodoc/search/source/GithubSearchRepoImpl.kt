package com.bronski.githubapiautodoc.search.source

import android.util.Log
import com.bronski.githubapiautodoc.core.api.GithubApi
import com.bronski.githubapiautodoc.core.utils.BaseResult
import javax.inject.Inject

class GithubSearchRepoImpl @Inject constructor(
    private val githubApi: GithubApi
) : IGithubSearchRepo {

    override suspend fun searchRepository(searchValue: String): BaseResult =
        runCatching {
            githubApi.getSearchRepoResult(name = searchValue)
        }.fold(onSuccess = {
            Log.d("TAGAAAAAA", "searchRepository: ${it.totalCount}")
            BaseResult.Success(it)
        }, onFailure = {
            BaseResult.Error(it.message.toString())
        })
}