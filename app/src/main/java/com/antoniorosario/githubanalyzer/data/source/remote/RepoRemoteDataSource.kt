package com.antoniorosario.githubanalyzer.data.source.remote

import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.RepoDataSource
import com.antoniorosario.githubanalyzer.network.GitHubApi
import com.antoniorosario.githubanalyzer.utils.ErrorUtils
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RepoRemoteDataSource(private val gitHubApi: GitHubApi) : RepoDataSource {

    override fun getRepos(organizationToQuery: String, callback: RepoDataSource.LoadReposCallback) {

        val call = gitHubApi.listRepos(organizationToQuery)
        call.enqueue(object : Callback<List<Repo>> {
            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.isSuccessful) {

                    // If repos == null that means organization has no repos, seems like a strange
                    // edge case. Keep this in mind just in case you ever get a NPE or empty screen.***giyhub***
                    val repos = response.body()!!
                    callback.onReposLoaded(repos, response)

                } else {

                    val error = ErrorUtils.parseError(response)
                    callback.onDataNotAvailable(error.message)
                }
            }

            override fun onFailure(call: Call<List<Repo>>, t: Throwable) {

                callback.onDataNotAvailable(t.message ?: "Unknown error")
            }
        })
    }

    fun getReposOnNextPage(nextDataSet: String, callback: RepoDataSource.LoadReposCallback) {

        val call = gitHubApi.listReposOnNextPage(nextDataSet)

        call.enqueue(object : Callback<List<Repo>> {

            override fun onResponse(call: Call<List<Repo>>, response: Response<List<Repo>>) {
                if (response.isSuccessful) {

                    // We return out of getReposOnNextPage in SearchPresenter if nextDataSet was null.
                    // If nextDataSet was null we can't reach this code and body will never be null.
                    val reposOnNextPage = response.body()!!
                    callback.onReposLoaded(reposOnNextPage, response)
                } else {

                    val error = ErrorUtils.parseError(response)
                    callback.onDataNotAvailable(error.message)

                }
            }

            override fun onFailure(call: Call<List<Repo>>?, t: Throwable) {
                callback.onDataNotAvailable(t.message ?: "Unknown error")
            }
        })
    }

}