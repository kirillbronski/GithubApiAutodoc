package com.bronski.githubapiautodoc.search.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bronski.githubapiautodoc.core.api.data.GithubResponse
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.utils.BaseResult
import com.bronski.githubapiautodoc.search.source.IGithubSearchRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val githubSearchRepoImpl: IGithubSearchRepo
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.DefaultState)
    val viewState: StateFlow<ViewState?> = _viewState.asStateFlow()

    private var search = MutableStateFlow("")

    fun searchFieldValue(
        search: String,
    ) {
        search.also {
            if (it != "") {
                this.search.value = it
            }
        }
        if (search.length >= 3) {
            getSearchResult()
        }
    }

    fun getSearchResult() {
        _viewState.value = ViewState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            githubSearchRepoImpl.searchRepository(search.value).run {
                when (this) {
                    is BaseResult.Success<*> -> {
                        when (responseResult) {
                            is GithubResponse -> {
                                _viewState.value =
                                    ViewState.SuccessState(this.responseResult.repo)
                            }
                        }
                    }
                    is BaseResult.Error -> {
                        _viewState.value = ViewState.ErrorState(this.errorMessage)
                    }
                }
            }
        }
    }
}