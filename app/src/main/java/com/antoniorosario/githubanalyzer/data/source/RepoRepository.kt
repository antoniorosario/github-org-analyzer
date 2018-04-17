package com.antoniorosario.githubanalyzer.data.source

import android.util.Log
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.local.RepoLocalDataSource
import com.antoniorosario.githubanalyzer.data.source.remote.RepoRemoteDataSource
import retrofit2.Response

/* Responsibility: Load + save repos searched by the user.*/
class RepoRepository(
        private val repoLocalDataSource: RepoLocalDataSource,
        private val repoRemoteDataSource: RepoRemoteDataSource
) : RepoDataSource {
    companion object {
        val LOG_TAG: String = RepoRepository::class.java.name


    }

    override fun getRepos(organizationToQuery: String, callback: RepoDataSource.LoadReposCallback) {
        // Query the local storage if available. If not, query the network.
        repoLocalDataSource.getRepos(organizationToQuery, object : RepoDataSource.LoadReposCallback {
            override fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?) {
                callback.onReposLoaded(repos, response)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                // We don't need to tell the user that there's no data in the database, log the message.
                Log.d(LOG_TAG, errorMessage)
                getReposFromRemoteDataSource(organizationToQuery, callback)
            }
        })
    }

    fun getReposFromRemoteDataSource(organization: String, callback: RepoDataSource.LoadReposCallback) {

        repoRemoteDataSource.getRepos(organization, object : RepoDataSource.LoadReposCallback {

            override fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?) {
                saveRepos(repos)
                callback.onReposLoaded(repos, response)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                callback.onDataNotAvailable(errorMessage)
            }
        })
    }

    fun getReposOnNextPage(nextDataSet: String, callback: RepoDataSource.LoadReposCallback) {

        repoRemoteDataSource.getReposOnNextPage(nextDataSet, object : RepoDataSource.LoadReposCallback {

            override fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?) {
                saveRepos(repos)
                callback.onReposLoaded(repos, response)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                callback.onDataNotAvailable(errorMessage)
            }
        })
    }

    fun saveRepos(repos: List<Repo>) {
        repoLocalDataSource.insertRepos(repos)
    }

}