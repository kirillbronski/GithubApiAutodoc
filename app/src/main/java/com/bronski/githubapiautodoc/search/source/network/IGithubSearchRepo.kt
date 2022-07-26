package com.bronski.githubapiautodoc.search.source.network

import androidx.paging.PagingData
import com.bronski.githubapiautodoc.core.api.data.Repository
import kotlinx.coroutines.flow.Flow

interface IGithubSearchRepo {
    suspend fun searchRepository(query: String): Flow<PagingData<Repository>>
}
