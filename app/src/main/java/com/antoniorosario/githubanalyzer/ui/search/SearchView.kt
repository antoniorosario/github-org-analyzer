package com.antoniorosario.githubanalyzer.ui.search

import android.arch.lifecycle.Lifecycle
import android.arch.lifecycle.LifecycleOwner
import com.antoniorosario.githubanalyzer.data.Repo

interface SearchView : LifecycleOwner {
    override fun getLifecycle(): Lifecycle
    fun showRepos(repos: List<Repo>)
    fun showErrorMessage(errorMessage: String)
    fun showErrorMessageAsToast(errorMessage: String)
    fun showLoadingIndicator()
    fun hideLoadingIndicator()
    fun clearSearchResults()
    fun hideSoftwareKeyboard()
}