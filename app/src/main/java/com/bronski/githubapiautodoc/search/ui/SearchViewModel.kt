package com.bronski.githubapiautodoc.search.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bronski.githubapiautodoc.core.api.data.Repository
import com.bronski.githubapiautodoc.search.source.network.IGithubSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
@HiltViewModel
class SearchViewModel @Inject constructor(
    private val githubSearchRepoImpl: IGithubSearchRepo,
) : ViewModel() {

    private var searchBy = MutableLiveData("")

    val repositoriesFlow: Flow<PagingData<Repository>> = searchBy.asFlow()
        .debounce(500)
        .flatMapLatest {
            githubSearchRepoImpl.searchRepository(it)
        }.cachedIn(viewModelScope)

    fun setSearchBy(value: String) {
        if (searchBy.value == value) return
        searchBy.value = value
    }

    fun swipeToUpdate() {
        searchBy.postValue(searchBy.value)
    }
}

