package com.bronski.githubapiautodoc.search.source.network

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bronski.githubapiautodoc.core.api.GithubApi
import com.bronski.githubapiautodoc.core.api.data.Repository
import com.bronski.githubapiautodoc.core.utils.Constants.PAGE_SIZE
import com.bronski.githubapiautodoc.search.source.SearchPagingSource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GithubSearchRepoImpl @Inject constructor(
    private val githubApi: GithubApi,
) : IGithubSearchRepo {

    override suspend fun searchRepository(query: String): Flow<PagingData<Repository>> {
        return Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                SearchPagingSource(githubApi = githubApi,
                    query = query,
                    pageSize = PAGE_SIZE)
            }
        ).flow
    }
}