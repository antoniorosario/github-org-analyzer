package com.antoniorosario.githubanalyzer.ui.detail


class RepoDetailPresenter {

    lateinit var repoDetailView: RepoDetailView
    fun onRepoUrlButtonClicked() {
        repoDetailView.goToRepoUrl()
    }
}