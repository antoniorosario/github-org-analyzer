package com.antoniorosario.githubanalyzer.ui.search

import com.antoniorosario.githubanalyzer.argumentCaptor
import com.antoniorosario.githubanalyzer.capture
import com.antoniorosario.githubanalyzer.data.Organization
import com.antoniorosario.githubanalyzer.data.Repo
import com.antoniorosario.githubanalyzer.data.source.RepoDataSource.LoadReposCallback
import com.antoniorosario.githubanalyzer.data.source.RepoRepository
import com.antoniorosario.githubanalyzer.eq
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.Mockito.verifyNoMoreInteractions
import org.mockito.MockitoAnnotations
import retrofit2.Response
import java.util.*


class SearchPresenterTest {

    @Mock private lateinit var mockSearchView: SearchView
    @Mock private lateinit var mockRepoRepository: RepoRepository
    @Mock private lateinit var mockSearchViewModel: SearchViewModel

    @Captor private lateinit var loadReposCallbackCaptor: ArgumentCaptor<LoadReposCallback>

    private lateinit var searchPresenter: SearchPresenter
    private lateinit var repos: MutableList<Repo>

    @Before fun setupSearchPresenterTest() {

        MockitoAnnotations.initMocks(this)
        searchPresenter = SearchPresenter(mockSearchView, mockRepoRepository, mockSearchViewModel)
        repos = Collections.singletonList(
                Repo(1, "Repo1", 50, 40, "Descrip", "url", Organization("GitHub"))
        )
    }

    @Test fun getRepos_shouldReturn_whenOrganizationIsAnEmptyString() {

        searchPresenter.getRepos("")
        verifyNoMoreInteractions(mockSearchView, mockRepoRepository, mockSearchViewModel)
    }

    @Test fun getRepos_shouldShowRepos_whenReposAreAvailable() {
        val organizationToQuery = "Github"
        val response: Response<List<Repo>> = Response.success(repos)

        searchPresenter.getRepos(organizationToQuery)

        verify(mockSearchViewModel).setOrganizationQueried(organizationToQuery)
        verify(mockSearchViewModel).setLoading(true)

        verify(mockSearchView).hideSoftwareKeyboard()
        verify(mockSearchView).clearSearchResults()
        verify(mockSearchView).showLoadingIndicator()

        verify(mockRepoRepository).getRepos(eq(organizationToQuery), capture(loadReposCallbackCaptor))

        loadReposCallbackCaptor.value.onReposLoaded(repos, response)

        verify(mockSearchViewModel).setLoading(false)
        verify(mockSearchViewModel).setCurrentResponse(response)

        verify(mockSearchView).hideLoadingIndicator()

        val showReposArgumentCaptor = argumentCaptor<List<Repo>>()
        verify(mockSearchView).showRepos(capture(showReposArgumentCaptor))
        assertTrue(showReposArgumentCaptor.value.size == repos.size)
    }

    @Test fun getRepos_shouldShowErrorMessage_whenReposAreUnavailable() {
        val organizationName = "Github"
        val errorMessage = "Error: error message"

        searchPresenter.getRepos(organizationName)

        verify(mockRepoRepository).getRepos(eq(organizationName), capture(loadReposCallbackCaptor))
        loadReposCallbackCaptor.value.onDataNotAvailable(errorMessage)

        mockSearchViewModel.setLoading(false)

        verify(mockSearchView).hideLoadingIndicator()
        verify(mockSearchView).showErrorMessage(errorMessage)
    }

    @Test
    fun getRepsOnNextPage_shouldNotRun_whenIsLoadingIsTrue() {
        val nextDataSet: String? = "https:api.github.com/orgs/organization/repos?page=50"
        val isLoading = true
        searchPresenter.getReposOnNextPage(isLoading, nextDataSet)
        verifyNoMoreInteractions(mockSearchViewModel, mockRepoRepository, mockSearchView)
    }

    @Test fun getRepsOnNextPage_shouldReturn_whenNextIsNull() {
        val nextDataSet: String? = null
        val isLoading = false

        searchPresenter.getReposOnNextPage(isLoading, nextDataSet)
        verifyNoMoreInteractions(mockSearchViewModel, mockRepoRepository, mockSearchView)
    }

    @Test fun getRepsOnNextPage_shouldShowRepos_whenReposAreAvailable() {
        val nextDataSet = "https:api.github.com/orgs/organization/repos?page=50"
        val isLoading = false
        val response: Response<List<Repo>> = Response.success(repos)

        searchPresenter.getReposOnNextPage(isLoading, nextDataSet)
        verify(mockSearchViewModel).setLoading(true)
        verify(mockSearchView).showLoadingIndicator()

        verify(mockRepoRepository).getReposOnNextPage(eq(nextDataSet), capture(loadReposCallbackCaptor))
        loadReposCallbackCaptor.value.onReposLoaded(repos, response)

        verify(mockSearchViewModel).setLoading(false)
        verify(mockSearchViewModel).setCurrentResponse(response)

        verify(mockSearchView).hideLoadingIndicator()

        val showReposArgumentCaptor = argumentCaptor<List<Repo>>()
        verify(mockSearchView).showRepos(capture(showReposArgumentCaptor))
        assertTrue(showReposArgumentCaptor.value.size == repos.size)
    }

    @Test fun getReposOnNextPage_shouldShowErrorMessage_whenReposAreUnavailable() {
        val nextDataSet = "https:api.github.com/orgs/organization/repos?page=50"
        val isLoading = false
        val errorMessage = "Error: error message"

        searchPresenter.getReposOnNextPage(isLoading, nextDataSet)

        verify(mockRepoRepository).getReposOnNextPage(eq(nextDataSet), capture(loadReposCallbackCaptor))
        loadReposCallbackCaptor.value.onDataNotAvailable(errorMessage)

        mockSearchViewModel.setLoading(false)

        verify(mockSearchView).hideLoadingIndicator()
        verify(mockSearchView).showErrorMessageAsToast(errorMessage)
    }
}