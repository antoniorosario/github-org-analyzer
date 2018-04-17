package com.antoniorosario.githubanalyzer.data.source.local

import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.RepoDataSource


class RepoLocalDataSource(private val repoDao: RepoDao) : RepoDataSource {
    override fun getRepos(organizationToQuery: String, callback: RepoDataSource.LoadReposCallback) {
//        val repos = repoDao.loadReposByOrganization(organizationToQuery)
        //TODO (3) Fix bug using Paging Library: when we load from the db we lose our current response.
        // We're giving an empty until we fix the bug
        val repos = emptyList<Repo>()
        if (repos.isEmpty()) {
            //Hard coded String is okay here because we're going to log the errorMessage.
            callback.onDataNotAvailable("No data in the database.")
        } else {
            callback.onReposLoaded(repos, null)
        }
    }

    fun insertRepos(repos: List<Repo>) {
        repoDao.insertRepos(repos)
    }
}