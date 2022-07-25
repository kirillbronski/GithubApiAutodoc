package com.bronski.githubapiautodoc.userdetails.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bronski.githubapiautodoc.core.api.data.UserResponseResult
import com.bronski.githubapiautodoc.core.state.ViewState
import com.bronski.githubapiautodoc.core.utils.BaseResult
import com.bronski.githubapiautodoc.userdetails.source.IGithubUserRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserDetailsViewModel @Inject constructor(
    private val githubUserRepo: IGithubUserRepo
) : ViewModel() {

    private val _viewState = MutableStateFlow<ViewState>(ViewState.DefaultState)
    val viewState: StateFlow<ViewState?> = _viewState.asStateFlow()

    fun getUserDetails(userName: String) {
        _viewState.value = ViewState.LoadingState
        viewModelScope.launch(Dispatchers.IO) {
            githubUserRepo.getUserDetails(userName).run {
                when (this) {
                    is BaseResult.Success<*> -> {
                        when (responseResult) {
                            is UserResponseResult -> {
                                _viewState.value = ViewState.SuccessUserState(this.responseResult)
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