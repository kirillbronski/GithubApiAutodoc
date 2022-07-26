package com.bronski.githubapiautodoc.userdetails.ui.screen

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.utils.BaseResult
import com.bronski.githubapiautodoc.userdetails.source.network.IGithubUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val githubUserRepo: IGithubUserRepo,
) : ViewModel() {

    private val _viewState = MutableLiveData<ViewState>(ViewState.DefaultState)
    val viewState: LiveData<ViewState> = _viewState

    fun getUserDetails(userName: String) {
        _viewState.value = ViewState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            githubUserRepo.getUserDetails(userName).run {
                when (this) {
                    is BaseResult.Success -> {
                        _viewState.postValue(ViewState.SuccessState(this.responseResult))
                    }
                    is BaseResult.Error -> {
                        _viewState.postValue(ViewState.ErrorState(this.errorMessage))
                    }
                }
            }
        }
    }
}