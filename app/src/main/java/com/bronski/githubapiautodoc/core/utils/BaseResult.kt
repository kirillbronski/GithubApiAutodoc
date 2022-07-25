package com.bronski.githubapiautodoc.core.utils

sealed class BaseResult {
    class Success<T>(val responseResult: T) : BaseResult()
    class Error(val errorMessage: String) : BaseResult()
}
