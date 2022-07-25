package com.bronski.githubapiautodoc.core.state

import com.bronski.githubapiautodoc.core.api.data.SearchResponseResult
import com.bronski.githubapiautodoc.core.api.data.UserResponseResult

sealed class ViewState {
    object DefaultState : ViewState()
    object LoadingState : ViewState()
    class SuccessState(val listRepo: MutableList<SearchResponseResult>) : ViewState()
    class SuccessUserState(val userResponseResult: UserResponseResult) : ViewState()
    class ErrorState(var message: String?) : ViewState()
}
