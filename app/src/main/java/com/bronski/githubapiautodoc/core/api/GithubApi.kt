package com.bronski.githubapiautodoc.core.api

import com.bronski.githubapiautodoc.core.api.data.GithubResponse
import com.bronski.githubapiautodoc.core.api.data.User
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApi {

    @GET("/search/repositories?")
    suspend fun getSearchRepoResult(
        @Query("q") searchValue: String = "",
        @Query("page") page: Int = 1,
        @Query("per_page") perPage: Int = 30,
    ): GithubResponse

    @GET("/users/{username}")
    suspend fun getUserInfo(
        @Path("username") username: String,
    ): User

}