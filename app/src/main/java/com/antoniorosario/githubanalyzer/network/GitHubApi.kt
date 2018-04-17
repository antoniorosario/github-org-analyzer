package com.antoniorosario.githubanalyzer.network

import com.antoniorosario.githubanalyzer.data.Repo
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Url

interface GitHubApi {
    /** Returns list of repos for the specified [organization]. */
    @GET("/orgs/{org}/repos")
    fun listRepos(@Path("org") organization: String): Call<List<Repo>>

    /** Returns list of repos for specified [url]. */
    @GET
    fun listReposOnNextPage(@Url url: String): Call<List<Repo>>
}