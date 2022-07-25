package com.bronski.githubapiautodoc.di.modules

import com.bronski.githubapiautodoc.core.api.GithubApi
import com.bronski.githubapiautodoc.search.source.GithubSearchRepoImpl
import com.bronski.githubapiautodoc.search.source.IGithubSearchRepo
import com.bronski.githubapiautodoc.userdetails.source.GithubUserRepoImpl
import com.bronski.githubapiautodoc.userdetails.source.IGithubUserRepo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepoModule {

    @Provides
    @Singleton
    fun provideGithubSearchRepo(githubApi: GithubApi): IGithubSearchRepo {
        return GithubSearchRepoImpl(githubApi = githubApi)
    }

    @Provides
    @Singleton
    fun provideGithubUserRepo(githubApi: GithubApi): IGithubUserRepo {
        return GithubUserRepoImpl(githubApi = githubApi)
    }
}