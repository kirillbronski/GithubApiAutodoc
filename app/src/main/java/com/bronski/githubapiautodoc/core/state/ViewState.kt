package com.bronski.githubapiautodoc.core.state

import com.bronski.githubapiautodoc.core.api.data.User

sealed class ViewState {
    object DefaultState : ViewState()
    object LoadingState : ViewState()
    class SuccessState(val user: User) : ViewState()
    class ErrorState(var message: String?) : ViewState()
}
