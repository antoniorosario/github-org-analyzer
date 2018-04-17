package com.antoniorosario.githubanalyzer.ui.search

import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.RepoDataSource
import com.antoniorosario.githubanalyzer.data.source.RepoRepository
import retrofit2.Response

/* Responsibility: Process data for UI */
class SearchPresenter(
        private val repoRepository: RepoRepository,
        internal val searchViewModel: SearchViewModel
) {

    lateinit var searchView: SearchView

    fun getRepos(organizationToQuery: String) {
        if (organizationToQuery.isEmpty()) return // If organization is empty  return out of the method

        searchViewModel.setOrganizationQueried(organizationToQuery)
        searchViewModel.setLoading(true)

        searchView.hideSoftwareKeyboard()
        searchView.clearSearchResults()
        searchView.showLoadingIndicator()

        repoRepository.getRepos(organizationToQuery, object : RepoDataSource.LoadReposCallback {
            override fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?) {

                searchViewModel.setLoading(false)
                searchViewModel.setCurrentResponse(response)

                searchView.hideLoadingIndicator()
                searchView.showRepos(repos)
            }

            override fun onDataNotAvailable(errorMessage: String) {
                searchViewModel.setLoading(false)

                searchView.hideLoadingIndicator()
                searchView.showErrorMessage(errorMessage)
            }
        })
    }

    fun getReposOnNextPage(isLoading: Boolean, nextDataSet: String?) {
        if (!isLoading) {

            // If nextDataSet is null we've already requested the last page or there isn’t any further data to
            //request. There’s nothing do to so we return out of the method.
            if (nextDataSet == null) return

            searchViewModel.setLoading(true)
            searchView.showLoadingIndicator()

            repoRepository.getReposOnNextPage(nextDataSet, object : RepoDataSource.LoadReposCallback {
                override fun onReposLoaded(repos: List<Repo>, response: Response<List<Repo>>?) {

                    searchViewModel.setCurrentResponse(response)
                    searchViewModel.setLoading(false)

                    searchView.hideLoadingIndicator()
                    searchView.showRepos(repos)
                }

                override fun onDataNotAvailable(errorMessage: String) {

                    searchViewModel.setLoading(false)

                    searchView.hideLoadingIndicator()
                    searchView.showErrorMessageAsToast(errorMessage)
                }
            })
        }
    }

}