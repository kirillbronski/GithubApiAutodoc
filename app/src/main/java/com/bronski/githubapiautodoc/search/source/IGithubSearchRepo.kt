package com.bronski.githubapiautodoc.search.source

import com.bronski.githubapiautodoc.core.utils.BaseResult

interface IGithubSearchRepo {
    suspend fun searchRepository(name: String): BaseResult
}
