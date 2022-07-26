package com.bronski.githubapiautodoc.search.source

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bronski.githubapiautodoc.core.api.GithubApi
import com.bronski.githubapiautodoc.core.api.data.Repository

class SearchPagingSource(
    private val githubApi: GithubApi,
    private val query: String,
    private val pageSize: Int,
) : PagingSource<Int, Repository>() {

    override fun getRefreshKey(state: PagingState<Int, Repository>): Int? {
        val anchorPosition = state.anchorPosition ?: return null
        val page = state.closestPageToPosition(anchorPosition) ?: return null
        return page.prevKey?.plus(1) ?: page.nextKey?.minus(1)
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Repository> {
        val pageIndex = params.key ?: 0

        return try {
            val responseApi = githubApi.getSearchRepoResult(searchValue = query,
                page = pageIndex,
                perPage = params.loadSize)
            return LoadResult.Page(
                data = responseApi.repositories,
                prevKey = if (pageIndex == 0) null else pageIndex - 1,
                nextKey = if (responseApi.repositories.size == params.loadSize) pageIndex + (params.loadSize / pageSize) else null
            )
        } catch (e: Exception) {
            LoadResult.Error(
                throwable = e
            )
        }
    }
}