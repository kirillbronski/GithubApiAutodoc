package com.bronski.githubapiautodoc.core.api

import com.bronski.githubapiautodoc.core.api.data.GithubResponse
import com.bronski.githubapiautodoc.core.api.data.UserResponseResult
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories?")
    suspend fun getSearchRepoResult(
        @Query("q") name: String = "",
        @Query("per_page") perPage: Int = 30,
        @Query("page") page: Int = 1,
    ): GithubResponse

    @GET("/users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String
    ): UserResponseResult

}