package com.antoniorosario.githubanalyzer.ui.search

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast.LENGTH_LONG
import android.widget.Toast.makeText
import com.antoniorosario.githubanalyzer.R
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.di.Injector
import com.antoniorosario.githubanalyzer.utils.PageLinksUtils
import kotlinx.android.synthetic.main.activity_repo_list.*
import org.parceler.Parcels

/* Responsibility: Drawing UI + receiving user interactions */
class SearchActivity : AppCompatActivity(), SearchView {
    companion object {
        const val PAGE_SIZE = 30
        const val KEY_EMPTY_STATE_TEXT = "empty_state_text"
        const val KEY_ORGANIZATION_QUERIED = "organization_queried"
        const val KEY_REPOS = "repos"
    }

    private lateinit var searchPresenter: SearchPresenter
    private lateinit var repoAdapter: RepoAdapter
    private lateinit var searchViewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_repo_list)

        searchPresenter = Injector.provideSearchPresenter(this)
        searchPresenter.searchView = this
        searchViewModel = searchPresenter.searchViewModel
        repoAdapter = Injector.provideSearchAdapter()
        initRecyclerView()
        initSearch()

        savedInstanceState?.let {
            val repos: List<Repo> = Parcels.unwrap(it.getParcelable(KEY_REPOS))
            // On rotation we should query the repository's local data source. If we don't have any
            // repos save locally then we query the remote data source. For now we're saving repos
            // on screen prior to rotation as a parcelable until we fix the bug in RepoLocalDataSource.
            // What we want to do -> onQueryTextSubmit(it.getString(KEY_ORGANIZATION_QUERIED))

            if (repos.isNotEmpty()) {
                clearSearchResults()
                repoAdapter.addRepos(repos)
            } else {
                empty_state_text_view.text = it.getString(KEY_EMPTY_STATE_TEXT)
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY_EMPTY_STATE_TEXT, empty_state_text_view.text.toString())
        outState.putParcelable(KEY_REPOS, Parcels.wrap(repoAdapter.repos))
    }

    private fun initRecyclerView() {
        val layoutManager = Injector.provideLayoutManager(this)
        repo_list_recycler_view.layoutManager = layoutManager
        repo_list_recycler_view.adapter = repoAdapter

        //set up pagination
        repo_list_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //TODO (3) remove this logic from activity and use Paging Library
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                if ((visibleItemCount + firstVisibleItemPosition) >= totalItemCount
                        && firstVisibleItemPosition >= 0
                        && totalItemCount >= PAGE_SIZE)
                    searchPresenter.getReposOnNextPage(
                            searchViewModel.isLoading.value!!,
                            // PageLinkUtils.next  is the next data subset, i.e., the url portion in
                            // the Link header. Example:
                            // <https://api.github.com/search/code?q=addClass+user%3Amozilla&page=2>; rel="next"
                            PageLinksUtils(searchViewModel.currentResponse.value?.headers()).next
                    )
            }
        })
    }

    private fun initSearch() {
        search_edit_text.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                onQueryTextSubmit(search_edit_text.text.toString().trim())
                return@OnEditorActionListener true
            }
            false
        })
    }

    private fun onQueryTextSubmit(organizationToQuery: String) {
        searchPresenter.getRepos(organizationToQuery)
    }

    override fun showRepos(repos: List<Repo>) {
        repoAdapter.addRepos(repos)
    }

    override fun hideSoftwareKeyboard() {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        this.currentFocus?.let {
            imm.hideSoftInputFromWindow(it.windowToken, 0)
        }
    }

    override fun showErrorMessage(errorMessage: String) {
        empty_state_text_view.text = getString(R.string.error_api_message, errorMessage)
    }

    override fun showErrorMessageAsToast(errorMessage: String) {
        makeText(this, errorMessage, LENGTH_LONG).show()
    }

    override fun showLoadingIndicator() {
        loading_indicator.visibility = View.VISIBLE
    }

    override fun hideLoadingIndicator() {
        loading_indicator.visibility = View.INVISIBLE
    }

    override fun clearSearchResults() {
        empty_state_text_view.text = ""
        repoAdapter.removeRepos()
    }
}