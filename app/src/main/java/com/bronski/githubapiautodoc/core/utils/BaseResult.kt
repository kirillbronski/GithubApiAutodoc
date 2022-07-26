package com.bronski.githubapiautodoc.core.utils

import com.bronski.githubapiautodoc.core.api.data.User

sealed class BaseResult {
    class Success(val responseResult: User) : BaseResult()
    class Error(val errorMessage: String) : BaseResult()
}
