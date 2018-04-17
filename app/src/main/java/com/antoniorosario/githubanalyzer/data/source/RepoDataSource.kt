package com.antoniorosario.githubanalyzer.data.source

import com.antoniorosario.githubanalyzer.data.Repo
import retrofit2.Response

interface RepoDataSource {

    interface LoadReposCallback {
        fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?)
        fun onDataNotAvailable(errorMessage: String)
    }

    fun getRepos(organizationToQuery: String, callback: LoadReposCallback)
}